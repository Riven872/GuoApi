## Docker 部署 Nacos 2.0.2（不是最新版）

1. 拉取镜像 `docker pull nacos/nacos-server:2.0.2`

2. 初始化 sql 脚本，新建 nacos_config 数据库，并运行脚本新建表

3. 创建本地 Nacos 日志和配置文件，目的是后面的操作将容器内的文件挂载到本地 

    1. `mkdir -p /home/nacos/logs`
    2. `mkdir -p /home/nacos/init.d`
    3. `mkdir -p /home/nacos/conf`

4. 修改配置文件 `vim /home/nacos/conf/application.properties`，修改其中的数据库链接地址

    ```properties
    spring.datasource.platform=mysql
    db.num=1
    db.url.0=jdbc:mysql://101.43.124.198:3306//nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC?allowPublicKeyRetrieval=true
    db.user=guoapi
    db.password=8myN7EPFAExHwdf8
     
    nacos.naming.empty-service.auto-clean=true
    nacos.naming.empty-service.clean.initial-delay-ms=50000
    nacos.naming.empty-service.clean.period-time-ms=30000
     
    management.endpoints.web.exposure.include=*
     
    management.metrics.export.elastic.enabled=false
    management.metrics.export.influx.enabled=false
     
    server.tomcat.accesslog.enabled=true
    server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i %{Request-Source}i
     
    server.tomcat.basedir=
     
    nacos.security.ignore.urls=/,/error,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-ui/public/**,/v1/auth/**,/v1/console/health/**,/actuator/**,/v1/console/server/**
     
    nacos.core.auth.system.type=nacos
    nacos.core.auth.enabled=false
    nacos.core.auth.default.token.expire.seconds=18000
    nacos.core.auth.default.token.secret.key=SecretKey012345678901234567890123456789012345678901234567890123456789
    nacos.core.auth.caching.enabled=true
    nacos.core.auth.enable.userAgentAuthWhite=false
    nacos.core.auth.server.identity.key=serverIdentity
    nacos.core.auth.server.identity.value=security
     
    nacos.istio.mcp.server.enabled=false
    ```

5. 启动 Nacos 容器，以单机模式启动 `docker run --name nacos -p 8848:8848 --restart=always -e JVM_XMS=256m -e JVM_XMX=256m -e MODE=standalone -v /home/nacos/logs:/home/nacos/logs -v /home/nacos/conf/application.properties:/home/nacos/conf/application.properties -d nacos/nacos-server`

6. 启动后输出容器的 uuid 则为启动成功

7. 访问 nacos 面板即可