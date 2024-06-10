package com.activiti7.activiti7imoocdevelop.controller;

import com.activiti7.activiti7imoocdevelop.enums.ResponseCode;
import com.activiti7.activiti7imoocdevelop.mapper.ActivitiMapper;
import com.activiti7.activiti7imoocdevelop.util.AjaxResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 用户接口
 *
 * @author debao.yang
 * @since 2024/6/10 12:47
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户接口", tags = {"用户接口"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final ActivitiMapper activitiMapper;

    @GetMapping(value = "getUsers")
    @ApiOperation(value = "获取用户列表", httpMethod = "GET")
    public AjaxResponse getUsers() {
        try {
            List<HashMap<String, Object>> list = activitiMapper.selectUser();
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取用户列表失败", e.toString());
        }
    }

}
