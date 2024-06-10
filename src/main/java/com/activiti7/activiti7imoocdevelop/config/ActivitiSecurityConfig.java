package com.activiti7.activiti7imoocdevelop.config;

import com.activiti7.activiti7imoocdevelop.enums.ResponseCode;
import com.activiti7.activiti7imoocdevelop.util.AjaxResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * ActivitiSecurityConfig
 *
 * @author 多宝
 * @since 2022/7/24 23:09
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivitiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public void configure(HttpSecurity security) {
        security.formLogin()
                // 登录方法
                .loginPage("/login")
                .loginProcessingUrl("/login")     // 未登录进行访问页面处理的接口路由
                .successHandler((request, response, authentication) -> {
                    // 登录成功的处理
                    response.setContentType("application/json;charset=UTF-8");
                    // response.getWriter()
                    //        .write("登录成功，用户名：" + authentication.getName());
                    response.getWriter()
                            .write(objectMapper.writeValueAsString(AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(),
                                    authentication.getName())));
                })
                .failureHandler((request, response, exception) -> {
                    // 登录失败的处理
                    response.setStatus(
                            HttpStatus.INTERNAL_SERVER_ERROR.value());

                    response.setContentType("application/json;charset=UTF-8");
                    // response.getWriter()
                    //        .write("登录失败，原因是" + exception.getMessage());
                    response.getWriter()
                            .write(objectMapper.writeValueAsString(AjaxResponse
                                    .ajaxData(
                                            ResponseCode.ERROR.getCode(),
                                            ResponseCode.ERROR.getDesc(),
                                            "登录失败，原因是：" + exception.getMessage())));
                })
                .and()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable();
    }
}
