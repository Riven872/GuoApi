## Dubbo 集成 Nacos Config

### Dubbo

#### 1、配置

```yaml
# 方法提供者
dubbo:
  scan:
    base-packages: com.edu.guoapi.service.provider # 这里写方法具体的实现类的引用路径
  application:
    name: guoapi-backend # 这里是注册到 Nacos 时，DataId 的名称
  protocol:
    name: dubbo # 使用的 RPC 协议
    port: 20881 # 使用的端口号，不指定的话从 20880 开始并默认向后迁移
  registry:
    address: nacos://101.43.124.198:8848 # 注册中心的地址
    parameters:
      namespace: 17f446fb-2cf9-47fd-a64b-7ecd0b37f823 # 注册中心的命名空间（注意不是 Group！！！）
```

#### 2、注册中心的配置管理（默认生成且以 Group 为区分点）

1. mapping：保存的是 Dubbo 应用暴露的服务信息以及服务的 URL 地址
2. dubbo：保存的是 Dubbo 应用的服务列表
3. 随机字符串：保存的是 Dubbo 应用在 Nacos 注册中心上注册的元数据信息，包括服务提供者的地址、协议、权重、负载均衡策略等信息，以及服务消费者的路由规则、负载均衡策略等信息。这些信息可以被 Dubbo 的注册中心和路由规则等组件用来实现服务发现、路由和负载均衡等功能。**说白了就是元信息**

#### 3、注册中心的服务列表（默认生成且以服务名为区分点）

1. 应用名：即将该应用自动注册到注册中心上
2. providers：
    1. 向外暴露的所有接口
    2. 元信息



### Nacos

#### 1、配置

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 101.43.124.198:8848 # 注册中心地址
        namespace: 17f446fb-2cf9-47fd-a64b-7ecd0b77f823 # 注册中心的命名空间
```

- 会将服务注册到服务列表中



### Chat 环节

```
Q:
在 Dubbo 2.7.0 及以上版本中，当使用 Nacos 作为注册中心时，讲一下 Nacos 的配置管理和服务管理中分别注册了什么信息
A:
当使用 Nacos 作为注册中心时，Dubbo 2.7.0 及以上版本中会将以下信息注册到 Nacos 的配置管理和服务管理中：
配置管理：Dubbo 将会向 Nacos 的配置管理模块注册应用的配置信息，包括应用名、端口、协议等配置信息，以及服务提供者和消费者的相关配置信息。
服务管理：Dubbo 将会向 Nacos 的服务管理模块注册应用提供的服务信息，包括服务接口、服务实现、服务分组、服务版本、服务地址、负载均衡策略、集群容错策略等信息。同时，Dubbo 会向 Nacos 的服务管理模块订阅其他应用提供的服务信息，用于消费者调用其他应用的服务。

Q:
再详细一点
A:
当使用 Nacos 作为 Dubbo 的注册中心时，Dubbo 会在 Nacos 的配置管理和服务管理中注册以下信息：
配置管理中注册的信息：
Data ID：dubbo-{group}-{interface}，其中 {group} 为 dubbo，{interface} 为 Dubbo 服务接口的全限定名；
Group：dubbo；
配置内容：Dubbo 服务的配置信息，例如 Dubbo 服务接口的方法信息、负载均衡策略、集群容错策略等。
服务管理中注册的信息：
服务名：Dubbo 服务接口的全限定名；
分组：Dubbo 服务分组，取值为 Dubbo 服务注解 @DubboService 中的 group 属性；
服务实例：Dubbo 服务实例的 IP 地址和端口号，由 Dubbo 的 RegistryProtocol 将 Dubbo 服务转换成 Nacos 服务实例并注册。
在 Nacos 的配置管理和服务管理中注册这些信息后，Dubbo 就可以通过 Nacos 发现和调用 Dubbo 服务。其中，配置管理中注册的信息用于向 Dubbo 服务提供者推送配置信息，服务管理中注册的信息用于向 Dubbo 服务消费者提供 Dubbo 服务实例信息。
```

