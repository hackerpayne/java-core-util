FROM registry.cn-shanghai.aliyuncs.com/hishop/openjkd:8u212-jre

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

RUN mkdir -p /code

WORKDIR /code

ADD ./himall-base/himall-base.jar /code/

EXPOSE 8080

CMD java -jar -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dspring.profiles.active=prod himall-base.jar

#CMD java -jar -Xms128m -Xmx128m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -Dspring.profiles.active=local himall-base.jar