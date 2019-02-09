package com.example.dubbo.services.impl;/**
 * Created by Administrator on 2018/11/14 0014.
 */

import com.example.dubbo.local.services.TestLocalService;
import com.example.dubbo.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-11-2018/11/14 0014 19:46
 */
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
