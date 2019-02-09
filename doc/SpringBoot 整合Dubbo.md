> SpringBoot 2.0 + Dubbo 2.6
> 
> [Provider端](https://github.com/oldguys/DubboProviderDemo)
> [Consumer端-1](https://github.com/oldguys/DubboClientDemo)
> [Consumer端-2](https://github.com/oldguys/DubboClientDemo2)

1. 三个项目引入Maven依赖
```
<dependency>
	<groupId>com.alibaba.boot</groupId>
	<artifactId>dubbo-spring-boot-starter</artifactId>
	<version>0.2.0</version>
</dependency>
```
2. 配置注册中心
  官方文档:  
http://dubbo.apache.org/zh-cn/docs/user/references/registry/redis.html

如果使用Redis配置方式，需要另外引入 jar
```
<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-pool2 -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
	<version>2.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
	<version>2.9.0</version>
</dependency>
```

3. 配置
##### provider：
配置Yaml
```
dubbo:
  protocol:
    name: dubbo
    port: 20880
  registry:
    address: zookeeper://127.0.0.1:2181
  application:
    name: dubbo-provider-demo1
#  scan:
#    base-packages: com.example.dubbo.services.impl
```
注解式配置包扫描
```
@Configuration
@DubboComponentScan(basePackages = "com.example.dubbo.services.impl")
public class DubboConfig {
}

```

##### Consumer
配置Yaml
```
dubbo:
  registry:
    address: zookeeper://127.0.0.1:2181
  application:
    name: dubbo-consumer-demo1
  consumer:
    timeout: 3000
```

4. 使用 
Dubbo接口的索引是类全路径，所以只是类名相同，则无效。
```
com.example.dubbo.services.TestService
```
创建Provider: 
使用注解
**@com.alibaba.dubbo.config.annotation.Service(timeout = 500)**
```
package com.example.dubbo.services.impl;

import com.example.dubbo.local.services.TestLocalService;
import com.example.dubbo.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@com.alibaba.dubbo.config.annotation.Service(timeout = 500)
public class TestServiceImpl implements TestService {

    @Autowired
    private TestLocalService testLocalService;

    @Override
    public String getMessage(String message) {
        return testLocalService.test() + "\t" + message;
    }
}

```

使用 **com.alibaba.dubbo.config.annotation.Reference** 进行引用
```
@RestController
public class TestClientController {

    @Reference
    private TestService testService;

    @GetMapping({"", "client"})
    public String test() {
        return testService.getMessage("-TestClientController");
    }
}
```
到此就完成了Dubbo的基本配置与使用

## 问题:
在Yaml文件中，有下列配置项：（**此配置项是否有效？**）
```
dubbo:
  scan:
    base-packages: com.example.dubbo.services.impl
```
该配置项对应源码
com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration
```
    /**
     * Creates {@link ServiceAnnotationBeanPostProcessor} Bean
     *
     * @param environment {@link Environment} Bean
     * @return {@link ServiceAnnotationBeanPostProcessor}
     */
    @ConditionalOnProperty(name = BASE_PACKAGES_PROPERTY_NAME)
    @ConditionalOnClass(ConfigurationPropertySources.class)
    @Bean
    public ServiceAnnotationBeanPostProcessor serviceAnnotationBeanPostProcessor(Environment environment) {
        Set<String> packagesToScan = environment.getProperty(BASE_PACKAGES_PROPERTY_NAME, Set.class, emptySet());
        return new ServiceAnnotationBeanPostProcessor(packagesToScan);
    }
```
通过 org.springframework.boot.context.properties.source.ConfigurationPropertySources 断点，可以确认是具备的。
也就是：
![源码截图.jpg](https://upload-images.jianshu.io/upload_images/14387783-700a2390b2703b5a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

由上图也可以看出参数都是具备，但并不会执行该处的代码。
而
 com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration
注入@Reference的代码却是有效的
```
    /**
     * Creates {@link ReferenceAnnotationBeanPostProcessor} Bean if Absent
     *
     * @return {@link ReferenceAnnotationBeanPostProcessor}
     */
    @ConditionalOnMissingBean
    @Bean(name = ReferenceAnnotationBeanPostProcessor.BEAN_NAME)
    public ReferenceAnnotationBeanPostProcessor referenceAnnotationBeanPostProcessor() {
        return new ReferenceAnnotationBeanPostProcessor();
    }
```
而当使用注解式的时候
```
@Configuration
@DubboComponentScan(basePackages = "com.example.dubbo.services.impl")
public class DubboConfig {
}

```
则上面那段代码中的类
com.alibaba.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationBeanPostProcessor
被使用了。
该实验似乎证明了上面 scanbasePackages 的Yaml配置是无效的。但无效却并没有被注释过期。
######所有有没有大佬可以解释一下上述线现象。