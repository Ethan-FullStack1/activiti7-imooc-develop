package com.activiti7.activiti7imoocdevelop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author 多宝
 * @since 2022/7/24 19:26
 */
@Api(value = "测试Controller", tags = {"测试Controller"})
@RestController
public class HelloController {

    @ApiOperation(value = "测试接口", httpMethod = "POST")
    @PostMapping("/hello")
    public String hello() {
        return "欢迎来到Activiti7";
    }

}
