package com.example.dubbo.controllers;/**
 * Created by Administrator on 2018/11/14 0014.
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: ren
 * @CreateTime: 2018-11-2018/11/14 0014 21:38
 */
@RestController
public class TestController {

    @GetMapping("test")
    public String test(){
        return "测试。。。。";
    }
}
