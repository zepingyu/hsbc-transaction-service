# openjdk21
FROM openjdk:21

# 设置工作目录
WORKDIR /app

# 将本地的 jar 文件复制到容器中
COPY /target/transaction-service-0.0.1-SNAPSHOT.jar /app/transaction-service-0.0.1.jar

# 暴露应用程序的端口
EXPOSE 8090

# 启动应用程序
ENTRYPOINT ["java", "-jar", "transaction-service-0.0.1.jar"]