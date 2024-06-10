package com.activiti7.activiti7imoocdevelop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户为经过Security登录进行访问资源处理
 *
 * @author 多宝
 * @since 2022/7/24 23:32
 */
@Api(value = "权限接口", tags = {"权限接口"})
@RestController
public class ActivitiSecurityController {

    @RequestMapping("/login")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ApiOperation(value = "登录接口", httpMethod = "POST")
    public String requireAuthentication(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam String username,
                                        String password) {
        return "需要登录，请使用login.html或发起post请求登录";
    }

}
