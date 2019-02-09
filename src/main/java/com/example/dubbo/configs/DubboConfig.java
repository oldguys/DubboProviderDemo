package com.example.dubbo.configs;/**
 * Created by Administrator on 2018/11/14 0014.
 */

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-11-2018/11/14 0014 19:44
 */
@Configuration
//@DubboComponentScan
@DubboComponentScan(basePackages = "com.example.dubbo.services.impl")
//@EnableDubbo
public class DubboConfig {

//    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("provider-test");
        return applicationConfig;
    }

//    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();

        // zookeeper
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setProtocol("dubbo");

//        registryConfig.setClient("curator");
        // redis
//        registryConfig.setAddress("127.0.0.1:6379");
//        registryConfig.setProtocol("redis");


//        registryConfig.setServer("curator");
//        registryConfig.setClient("curator");
        return registryConfig;
    }
}
