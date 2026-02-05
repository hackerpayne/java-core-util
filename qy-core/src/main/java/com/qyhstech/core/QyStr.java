package com.qyhstech.core;

import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kyle on 17/3/28.
 */
@Slf4j
public class QyStr extends StrUtil {

    /**
     * 空值
     */
    public static final String EMPTY = "";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 斜杠
     */
    public static final String SLASH = "/";

    /**
     * 获取patter的过程较为负责,这里初始化时,做一次即可
     */
    private static Pattern pattern;

    static {
        pattern = Pattern.compile("((?<=\\{)([a-zA-Z_]{1,})(?=\\}))");
    }

    /**
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    /**
     * @param object
     * @return
     */
    public static boolean notNull(Object object) {
        return Objects.nonNull(object);
    }


    /**
     * 过滤emoji表情
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        return filterEmoji(source, "");
    }

    /**
     * emoji表情替换
     *
     * @param source  原字符串
     * @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source, String slipStr) {
        if (QyStr.isNotBlank(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        } else {
            return source;
        }
    }

    /**
     * 给身份证或者姓名简单加密
     *
     * @param str
     * @param startLen
     * @param endLen
     * @return
     */
    public static String addStart(String str, int startLen, int endLen) {

        if (QyStr.isNotBlank(str)) {
            int len = QyStr.trim(str).length();

            switch (len) {
                case 2:
                    str = "*" + str.substring(1, 2);
                    break;
                case 3:
                    str = str.substring(0, 1) + "*" + str.substring(2, 3);
                    break;
                case 4:
                    str = str.substring(0, 2) + "*" + str.substring(3, 4);
                    break;
                default:
                    int startsCount = len - (startLen + endLen);
                    str = str.substring(0, startLen) + QyStr.repeat("*", startsCount) + StrUtil.sub(str, str.length() - endLen, str.length());
                    break;
            }
        }
        return str;
    }


    /**
     * 隐藏姓名
     *
     * @param name
     * @return
     */
    public static String hideName(String name) {
        if (StrUtil.isEmpty(name)) {
            return EMPTY;
        }
        name = name.trim();
        if (name.length() == 2) {
            name = StrUtil.sub(name, 0, 1) + "*";
        } else if (name.length() == 3) {
            name = StrUtil.sub(name, 0, 1) + "*" + StrUtil.sub(name, 1, 2);
        } else {
            String firstName = StrUtil.sub(name, 0, 1);
            String lastName = StrUtil.sub(name, name.length() - 1, name.length());

            StringBuffer sb = new StringBuffer();
            for (int i = 1; i < name.length() - 1; i++) {
                sb.append("*");
            }
            name = firstName + sb.toString() + lastName;
        }
        return name;
    }

    /**
     * 隐藏身份证号码
     *
     * @param idcard
     * @return
     */
    public static String hideIdcard(String idcard) {
        if (QyStr.isNotBlank(idcard)) {
            String firstThree = StrUtil.sub(idcard, 0, 3);
            String lastFour = StrUtil.sub(idcard, idcard.length() - 4, idcard.length());
            String starts = "";
            for (int i = 3; i < idcard.length() - 4; i++) {
                starts += "*";
            }
            idcard = firstThree + starts + lastFour;
        }
        return idcard;
    }

    /**
     * 隐藏手机号
     *
     * @param mobile
     * @return
     */
    public static String hideMobile(String mobile) {
        if (QyStr.isNotBlank(mobile)) {
            String firstThree = StrUtil.sub(mobile, 0, 3);
            String lastFour = StrUtil.sub(mobile, mobile.length() - 4, mobile.length());
            String starts = "";
            for (int i = 3; i < mobile.length() - 4; i++) {
                starts += "*";
            }
            mobile = firstThree + starts + lastFour;
        }
        return mobile;
    }

    /**
     * @param str
     * @param searchChar
     * @return
     */
    public static boolean contains(String str, char searchChar) {
        return !isEmpty(str) && str.indexOf(searchChar) >= 0;
    }

    /**
     * @param str
     * @param searchStr
     * @return
     */
    public static boolean contains(String str, String searchStr) {
        return (str != null && searchStr != null) && str.contains(searchStr);
    }

    /**
     * 判断字符串是否包含集合中的指定数量的字符串
     * 比如：列表中至少有3个信息，在字符串中存在
     *
     * @param input      字符串
     * @param list       要判断的列表
     * @param judgeCount 要判断的条数
     * @return
     */
    public static boolean containsAtLeast(String input, Collection<String> list, int judgeCount) {
        if (StrUtil.isEmpty(input)) {
            return false;
        }

        Set<String> uniqueElements = new HashSet<>(list);
        int count = 0;
        for (String element : uniqueElements) {
            if (input.contains(element)) {
                if (++count >= judgeCount) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从List里面根据Contains进行匹配字符串，如果包含指定的字符串就清理掉这条记录
     * 比如：hahaha,12354，如果包含hah就清理，最后只会剩下一条记录12354
     * 例：removeDuplicateByContains(listLinks,new String[]{"m.liebiao.com","#","about.liebiao.com"});
     *
     * @param list
     * @param contains
     * @return
     */
    public static List<String> removeDuplicateByContains(List<String> list, String... contains) {
        // 遍历进行数据清洗
        Iterator<String> it = list.iterator();

        boolean isContains;
        while (it.hasNext()) {
            String x = it.next();
            isContains = false;

            for (String item : contains) {
                if (x.contains(item)) {
                    isContains = true;
                    break;
                }
            }

            if (isContains) {
                it.remove();
            }
        }
        return list;
    }

    /**
     * 移除字符串中所有给定字符串<br>
     * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
     *
     * @param str         字符串
     * @param strToRemove 被移除的字符串
     * @return 移除后的字符串
     */
    public static String removeAll(String str, CharSequence strToRemove) {
        return str.replace(strToRemove, EMPTY);
    }

    /**
     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
     * 例如：str=setName, preLength=3 =》 return name
     *
     * @param str       被处理的字符串
     * @param preLength 去掉的长度
     * @return 处理后的字符串，不符合规范返回null
     */
    public static String removePreAndLowerFirst(CharSequence str, int preLength) {
        if (str == null) {
            return null;
        }

        if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            if (str.length() > preLength + 1) {
                return first + str.toString().substring(preLength + 1);
            }
            return String.valueOf(first);
        } else {
            return str.toString();
        }


    }

    /**
     * 删除(替换)不可见的unicode/utf-8字符
     *
     * @param str
     * @return
     */
    public static String checkStr(String str) {
        String s = null;
        char[] cc = str.toCharArray();
        for (int i = 0; i < cc.length; i++) {
            boolean b = QyJudge.isValidChar(cc[i]);
            if (!b) {
                cc[i] = ' ';
            }
        }
        s = String.valueOf(cc);
        return s.trim();
    }

    /**
     * 删除最后一个字符为特定字符的结果
     *
     * @param input
     * @param end
     * @return
     */
    public static String removeEnd(String input, String end) {
        if (isEmpty(input) || isEmpty(end)) {
            return input;
        }
        return removeSuffix(input, end);
    }

    /**
     * 删除指定的开始字符
     *
     * @param input 输入字符串
     * @param start 要删除的首位字符串
     * @return
     */
    public static String removeStart(String input, String start) {
        if (isEmpty(input) || isEmpty(start)) {
            return input;
        }
        return removePrefix(input, start);
    }

    /**
     * 删除指定的开头的字符串
     *
     * @param input 输入字符串
     * @param start 要删除的字符串
     * @return
     */
    public static String trimStartByRegex(String input, String start) {
        // null或者空字符串的时候不处理
        if (isEmpty(input) || isEmpty(start)) {
            return input;
        }
        // 要删除的字符串结束位置
        int end;
        // 正规表达式
        String regPattern = "[" + start + "]*+";
        Pattern pattern = Pattern.compile(regPattern, Pattern.CASE_INSENSITIVE);
        // 去掉原始字符串开头位置的指定字符
        Matcher matcher = pattern.matcher(input);
        if (matcher.lookingAt()) {
            end = matcher.end();
            input = input.substring(end);
        }
        // 返回处理后的字符串
        return input;
    }

    /**
     * 判断是否所有数据都是非空，如果都是非空，返回true，任何一个是空的，返回false
     *
     * @param listDatas
     * @return
     */
    public static boolean isAllNotEmpty(String... listDatas) {
        boolean flag = true;
        for (String data : listDatas) {
            if (isEmpty(data)) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean containsAny(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 批量替换变量
     *
     * @param text
     * @param map
     * @return
     */
    public String replaceV3(String text, Map<String, Object> map) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String key = matcher.group();
            text = text.replaceAll("\\{" + key + "\\}", map.get(key) + "");
        }

        return text;
    }

    /**
     * 批量替换变量为指定的结果
     *
     * @param text
     * @param mapList
     * @return
     */
    public List<String> replaceV4(String text, List<Map<String, Object>> mapList) {
        List<String> keys = new ArrayList<>();

        // 把文本中的所有需要替换的变量捞出来, 丢进keys
        Matcher matcher = pattern.matcher(text);
        int index = 0;
        while (matcher.find()) {
            String key = matcher.group();
            if (!keys.contains(key)) {
                keys.add(key);
                // 开始替换, 将变量替换成数字,
                text = text.replaceAll(keys.get(index), index + "");
                index++;
            }
        }


        List<String> result = new ArrayList<>();
        //  从map中将对应的值丢入 params 数组
        Object[] params = new Object[keys.size()];
        for (Map<String, Object> map : mapList) {
            for (int i = 0; i < keys.size(); i++) {
                params[i] = map.get(keys.get(i) + "");
            }

            result.add(replace(text, params));
        }
        return result;
    }

    /**
     * @param text
     * @param args
     * @return
     */
    public String replace(String text, Object... args) {
        return MessageFormat.format(text, args);
    }

    /**
     * 直接把Object转为指定的类型, 避免强转时抛出异常
     *
     * @param obj
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num  数字对象
     * @param size 字符串指定长度，不够长度的左边补0
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static String padLeft(final Number num, final int size) {
        return padLeft(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     * 比如：
     * padl("abc", 5, '*')  返回"***abc"
     *
     * @param s    原始字符串
     * @param size 字符串指定长度
     * @param c    用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static String padLeft(final String s, final int size, final char c) {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            final int len = s.length();
            if (s.length() <= size) {
                for (int i = size - len; i > 0; i--) {
                    sb.append(c);
                }
                sb.append(s);
            } else {
                return s.substring(len - size, len);
            }
        } else {
            for (int i = size; i > 0; i--) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 根据冒号来进行对齐。左边左对齐，右边按左边最长的宽度进行左对齐
     *
     * @param input
     * @return
     */
    public static String paddingBySemiColon(String input) {
        return paddingBySemiColon(StrUtil.split(input, "\n"));
    }

    /**
     * 根据冒号来进行对齐。左边左对齐，右边按左边最长的宽度进行左对齐
     *
     * @param lines
     * @return
     */
    public static String paddingBySemiColon(List<String> lines) {
        StringBuilder sb = new StringBuilder();

        // 计算左边部分的最长宽度
        int maxLeftWidth = 0;
        for (String line : lines) {
            line = line.trim();
            String[] parts = line.split(COLON, 2);
            if (parts.length == 2) {
                maxLeftWidth = Math.max(maxLeftWidth, parts[0].trim().length());
            }
        }

        // 处理每一行
        for (String line : lines) {
            line = line.trim();
            // 按第一个冒号分割
            String[] parts = line.split(COLON, 2);

            if (parts.length == 2) {
                String leftPart = parts[0].trim();
                String rightPart = parts[1].trim();

                // 构建格式化的行
                StringBuilder formattedLine = new StringBuilder();

                // 添加左边部分
                formattedLine.append(leftPart);

                // 补充空格到最长左边宽度
                int leftPadding = maxLeftWidth - leftPart.length();
                for (int i = 0; i < leftPadding; i++) {
                    formattedLine.append(" ");
                }

                // 添加冒号和一个空格
                formattedLine.append(" : ");

                // 添加右边部分
                formattedLine.append(rightPart);

                // 输出格式化后的行
                sb.append(formattedLine).append("\n");
            } else {
                // 如果行中没有冒号，直接打印
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 使用正则进行拆分，拆分之后清理空值并Trim前后的值
     *
     * @param text           要处理的字符串
     * @param separatorRegex 正则表达式
     * @return
     */
    public static List<String> splitByRegex(String text, String separatorRegex) {
        return StrSplitter.splitByRegex(text, separatorRegex, 0, true, true);
    }

    /**
     * 批量替换
     *
     * @param text
     * @param searchList
     * @param replacementList
     * @return
     */
    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        if (text == null || searchList == null || replacementList == null) {
            return text;
        }
        String result = text;
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] != null && replacementList[i] != null) {
                result = StrUtil.replace(result, searchList[i], replacementList[i]);
            }
        }
        return result;
    }

}
