### 记录项目的踩坑

1. 新版 SpringCloud Gateway + Nacos 没有集成 Ribbon，因此在网关断言之后负载均衡时，会报 503 错误，且没有报错。需要手动引入负载均衡的 Jar 包。

    ```maven
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
    ```

2. 在使用 Mybatis 操作数据库时，报以下的错

    ```log
    Creating a new SqlSession
    SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@1909592f] was not registered for synchronization because synchronization is not active
    JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@13995419] will not be managed by Spring
    ```

    是因为实现类的方法上没有添加 @transactional 注解，从而导致未同步注册。

3. 报错信息

    ```log
    HikariPool-1 - Failed to validate connection com.mysql.cj.jdbc.ConnectionImpl@68c2b81a (No operations allowed after connection closed.). Possibly consider using a shorter maxLifetime value.
    ```

    当用 springboot 连接数据库的时候，会建立一个和数据库的连接，这个连接保存在数据库连接池中，现在你的这个连接已经 time out 已经不能用了，但是你的这个连接还是保存在数据库连接池中，springboot 仍然使用这个连接去连接数据库，所以才会报错。

    连接时间，即 SpringBoot 连接要保存在连接池中多长时间才会被清除。假如连接时间设置 5min，连接在 3min 超时的时候还得 2min 才会被清除。

    总结就是：springboot 用的连接池中的连接（因为连接池用的连接还未超时，定义时间），超过了数据库的最大连接超时时间

    解决：我们设置连接的存活时间是 2min，连接就一直处于可用状态，当 2min 之后你再次访问数据库，就会 new 一个连接，这个连接在其生命周期内仍然是可用的。即 `spring.datasource.hikari.max-lifetime=120000`