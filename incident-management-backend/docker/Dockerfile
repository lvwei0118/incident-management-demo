# 基础镜像
FROM ubuntu:latest
 
# 更新软件包列表
RUN apt-get update
 
# 安装OpenJDK 17
RUN apt-get install -y openjdk-17-jdk

# 安装 locales 并设置 UTF-8
RUN apt-get update \
    && apt-get install -y locales \
    && locale-gen en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝jar包
COPY management-0.0.1-SNAPSHOT.jar app.jar
# 入口
CMD java -jar app.jar