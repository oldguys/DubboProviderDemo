package com.example.dubbo.local.services;

import org.springframework.stereotype.Service;

/**
 * @Date: 2019/2/6 0006
 * @Author: ren
 * @Description:
 */
@Service
public class TestLocalService {

    public String test() {
        return TestLocalService.class.getCanonicalName();
    }
}
