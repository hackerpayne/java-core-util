#!/usr/bin/env bash
set -euo pipefail

###############################################################################
# 基本配置
###############################################################################

# ENV=test JVM_PARAMS="-Xms256m -Xmx256m -Dspring.profiles.active=test" ./spring.sh start
# ENV=test ./spring.sh start

# Spring 环境
ENV="${ENV:-prod}"

# 部署目录（脚本所在目录）
DEPLOY_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# JVM 参数（注意顺序：选项在 -jar 之前）
JVM_PARAMS="${JVM_PARAMS:--Xms512m -Xmx512m -Dspring.profiles.active=${ENV}}"

# 目录配置
LIB_DIR="${LIB_DIR:-${DEPLOY_DIR}/lib}"
CONF_DIR="${CONF_DIR:-${DEPLOY_DIR}/config}"
LOGS_DIR="${LOGS_DIR:-${DEPLOY_DIR}/logs}"
BACKUP_DIR="${BACKUP_DIR:-${DEPLOY_DIR}/backup}"
PID_FILE="${PID_FILE:-${DEPLOY_DIR}/spring.pid}"

# Java 命令（可通过环境变量覆盖）
JAVA_CMD="${JAVA_CMD:-java}"

###############################################################################
# 工具函数
###############################################################################

log_info() {
  echo "[INFO] $*"
}

log_error() {
  echo "[ERROR] $*" >&2
}

error_exit() {
  log_error "$*"
  exit 1
}

create_directories() {
  local dirs=("$LIB_DIR" "$CONF_DIR" "$LOGS_DIR" "$BACKUP_DIR")
  for dir in "${dirs[@]}"; do
    if [[ ! -d "$dir" ]]; then
      mkdir -p "$dir" || error_exit "无法创建目录: $dir"
      log_info "创建目录: $dir"
    fi
  done
}

# 寻找 JAR（默认取最新的一个）
find_jar() {
  local jar
  jar="$(find "$LIB_DIR" -maxdepth 1 -type f -name "*.jar" -printf "%T@ %p\n" \
        | sort -nr \
        | head -n1 \
        | awk '{print $2}')"

  if [[ -z "${jar:-}" ]]; then
    error_exit "未在 $LIB_DIR 找到 JAR 文件"
  fi

  echo "$jar"
}

# 校验 PID 是否对应当前 JAR 进程
is_running() {
  local pid="$1"
  if ! ps -p "$pid" > /dev/null 2>&1; then
    return 1
  fi
  # 可选：确认是当前 JAR 的进程（更安全）
  if ps -p "$pid" -o args= | grep -q "$PROG_FILE"; then
    return 0
  fi
  return 1
}

###############################################################################
# 环境初始化
###############################################################################

create_directories

# 加载 Java 环境
if [[ -f /etc/profile ]]; then
  # shellcheck source=/etc/profile
  source /etc/profile
fi

if [[ -f "$HOME/.sdkman/bin/sdkman-init.sh" ]]; then
  # shellcheck source=/dev/null
  source "$HOME/.sdkman/bin/sdkman-init.sh"
  sdk use java 21.0.2-open >/dev/null 2>&1 || true
fi

JAR_NAME="$(find_jar)"
PROG_FILE="$JAR_NAME"

export CLASSPATH="${CLASSPATH:-}:$DEPLOY_DIR/:$CONF_DIR/"

echo "JAR FILE Location : $JAR_NAME"
echo "Current Deploy Path: $DEPLOY_DIR"
echo "Export Log Path    : $LOGS_DIR"

cd "$DEPLOY_DIR" || error_exit "无法进入目录: $DEPLOY_DIR"

###############################################################################
# 核心操作
###############################################################################

start() {
  if [[ -f "$PID_FILE" ]]; then
    local old_pid
    old_pid="$(cat "$PID_FILE" || true)"

    if [[ -n "$old_pid" ]] && is_running "$old_pid"; then
      echo "$PROG_FILE 已在运行, PID: $old_pid"
      return 0
    else
      log_info "发现失效 PID 文件, 删除: $PID_FILE"
      rm -f "$PID_FILE"
    fi
  fi

  log_info "正在启动 Java 应用..."
  mkdir -p "$LOGS_DIR"

  # 关键修正：JVM 参数在 -jar 之前
  nohup "$JAVA_CMD" $JVM_PARAMS -jar "$PROG_FILE" >/dev/null 2>&1 &

  local new_pid=$!
  echo "$new_pid" > "$PID_FILE"
  log_info "启动成功, PID: $new_pid"
}

backup() {
  local time_stamp filename extension name_only backup_filename

  time_stamp="$(date +"%Y%m%d_%H%M%S")"
  filename="$(basename "$JAR_NAME")"
  extension="${filename##*.}"
  name_only="${filename%.*}"
  backup_filename="${name_only}_${time_stamp}.${extension}"

  if cp "$JAR_NAME" "$BACKUP_DIR/$backup_filename"; then
    echo "备份成功: $BACKUP_DIR/$backup_filename"
  else
    echo "备份失败"
    return 1
  fi
}

stop() {
  echo "正在停止 $PROG_FILE ..."

  if [[ ! -f "$PID_FILE" ]]; then
    echo "PID 文件不存在: $PID_FILE"
    return 0
  fi

  local pid
  pid="$(cat "$PID_FILE" || true)"

  if [[ -z "$pid" ]]; then
    echo "PID 文件为空, 删除之"
    rm -f "$PID_FILE"
    return 0
  fi

  if ! is_running "$pid"; then
    echo "PID $pid 对应的进程不存在, 删除 PID 文件"
    rm -f "$PID_FILE"
    return 0
  fi

  kill "$pid" >/dev/null 2>&1 || true

  local count=0
  local max_retries=30
  while (( count < max_retries )); do
    if ! is_running "$pid"; then
      echo "已正常停止"
      rm -f "$PID_FILE"
      return 0
    fi
    echo -n "."
    sleep 1
    ((count++))
  done

  echo ""
  echo "超时，执行强制 kill -9 $pid ..."
  kill -9 "$pid" >/dev/null 2>&1 || true
  rm -f "$PID_FILE"
  echo "已强制停止"
}

restart() {
  stop
  start
}

restarts() {
  stop
  start
  log
}

restartb() {
  stop
  backup
  start
  log
}

starts() {
  start
  log
}

status() {
  if [[ -f "$PID_FILE" ]]; then
    local pid
    pid="$(cat "$PID_FILE" || true)"
    if [[ -n "$pid" ]] && is_running "$pid"; then
      echo "$PROG_FILE 正在运行 (PID: $pid)"
      return 0
    fi
  fi
  echo "$PROG_FILE 未运行"
}

###############################################################################
# 命令分发
###############################################################################

case "${1:-}" in
  start)     start ;;
  starts)    starts ;;
  stop)      stop ;;
  restart)   restart ;;
  restarts)  restarts ;;
  restartb)  restartb ;;
  status)    status ;;
  backup)    backup ;;
  *)
    cat <<EOF
Usage: $0 <command>

Commands:
  start        启动应用（后台运行）
  starts       启动应用并实时输出日志（相当于：start + log）
  stop         停止应用
  restart      重启应用（stop + start）
  restarts     重启并实时查看日志（restart + log）
  restartb     重启前先备份当前 JAR（stop + backup + start + log）
  status       查看当前运行状态
  log          持续查看日志（tail -f）
  backup       备份当前正在使用的 JAR 文件

示例：
  $0 start
  $0 restart
  $0 backup
  $0 log
  ENV=test JVM_PARAMS="-Xms256m -Xmx256m -Dspring.profiles.active=test" $0 start

EOF
    exit 2
    ;;
esac

exit 0