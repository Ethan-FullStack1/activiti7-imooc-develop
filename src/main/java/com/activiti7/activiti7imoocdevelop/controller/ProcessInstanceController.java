package com.activiti7.activiti7imoocdevelop.controller;

import com.activiti7.activiti7imoocdevelop.common.Constants;
import com.activiti7.activiti7imoocdevelop.enums.ResponseCode;
import com.activiti7.activiti7imoocdevelop.pojo.UserInfoBean;
import com.activiti7.activiti7imoocdevelop.util.AjaxResponse;
import com.activiti7.activiti7imoocdevelop.util.DateUtil;
import com.activiti7.activiti7imoocdevelop.util.GlobalConfig;
import com.activiti7.activiti7imoocdevelop.util.SecurityUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 流程实例接口
 *
 * @author debao.yang
 * @since 2022/7/29 17:12
 */
@RestController
@RequestMapping("/processInstance")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "流程实例相关接口", tags = {"流程实例相关接口"})
public class ProcessInstanceController {

    private final RepositoryService repositoryService;
    private final SecurityUtil securityUtil;
    private final ProcessRuntime processRuntime;

    // 查询流程实例
    @GetMapping("/getInstances")
    @ApiOperation(value = "获取流程实例", httpMethod = "GET")
    public AjaxResponse getInstances(
            @AuthenticationPrincipal @ApiIgnore UserInfoBean userInfoBean) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs(Constants.USER_BAJIE);
            }
            // else {
            //     securityUtil.logInAs(userInfoBean.getUsername());
            // }

            // 这个是Activit7的方法，在使用这个方法之前，一定要登录一下
            Page<ProcessInstance> processInstances =
                    processRuntime.processInstances(Pageable.of(0, 100));
            List<ProcessInstance> list = processInstances.getContent();
            list.sort(Comparator.comparing(x -> x.getStartDate().toString()));

            List<Map<String, Object>> listMap = Lists.newArrayList();

            list.forEach(item -> {
                Map<String, Object> hashMap = Maps.newHashMap();
                hashMap.put("id", item.getId());
                hashMap.put("name", item.getName());
                hashMap.put("status", item.getStatus());
                hashMap.put("processDefinitionId",
                        item.getProcessDefinitionId());
                hashMap.put("processDefinitionKey",
                        item.getProcessDefinitionKey());
                hashMap.put("startDate",
                        DateUtil.formatDefault(item.getStartDate()));
                hashMap.put("processDefinitionVersion",
                        item.getProcessDefinitionVersion());
                // 因为item里面没有历史高亮需要的deploymentId和资源Name，所以需要再次查询
                ProcessDefinition processDefinition =
                        repositoryService.createProcessDefinitionQuery()
                                .processDefinitionId(
                                        item.getProcessDefinitionId())
                                .singleResult();
                hashMap.put("deploymentId",
                        processDefinition.getDeploymentId());
                hashMap.put("resourceName",
                        processDefinition.getResourceName());

                listMap.add(hashMap);
            });

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取流程实例失败", e.toString());
        }
    }


    // 启动流程实例
    @GetMapping("/startProcess")
    @ApiOperation(value = "启动流程实例", httpMethod = "GET")
    public AjaxResponse startProcess(@RequestParam String processDefinitionKey,
                                     @RequestParam String instanceName) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs(Constants.USER_BAJIE);
            } else {
                securityUtil.logInAs(SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName());
            }

            ProcessInstance instance = processRuntime.start(
                    ProcessPayloadBuilder.start()
                            .withProcessDefinitionKey(processDefinitionKey)
                            .withName(instanceName)
                            .withBusinessKey("自定义BusinissKey")
                            .build());


            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    instance.getName());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "启动流程实例失败", e.toString());
        }
    }

    // 挂起流程实例
    @GetMapping("/suspendInstance")
    @ApiOperation(value = "挂起流程实例", httpMethod = "GET")
    public AjaxResponse suspendInstance(@RequestParam String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs(Constants.USER_BAJIE);
            }

            ProcessInstance suspend =
                    processRuntime.suspend(ProcessPayloadBuilder.suspend()
                            .withProcessInstanceId(
                                    instanceID)
                            .build());
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    suspend.getName());

        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "暂停流程实例失败", e.toString());
        }
    }

    // 激活/重启流程实例
    @GetMapping("/resumeInstance")
    @ApiOperation(value = "激活/重启流程实例",httpMethod = "GET")
    public AjaxResponse resumeInstance(@RequestParam String instanceID) {

        if (GlobalConfig.Test) {
            securityUtil.logInAs(Constants.USER_BAJIE);
        }

        try {
            ProcessInstance instance = processRuntime.resume(
                    ProcessPayloadBuilder.resume()
                            .withProcessInstanceId(instanceID)
                            .build());
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    instance.getName());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "删除流程实例失败", e.toString());
        }

    }

    // 删除流程实例
    @GetMapping("/deleteInstance")
    @ApiOperation(value = "删除流程实例", httpMethod = "POST")
    public AjaxResponse deleteInstance(@RequestParam String instanceId) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs(Constants.USER_BAJIE);
            }

            ProcessInstance instance = processRuntime.delete(
                    ProcessPayloadBuilder.delete()
                            .withProcessInstanceId(instanceId).build());
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    instance.getName());

        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "删除流程实例失败", e.toString());
        }

    }

    // 查询流程参数
    @GetMapping("/variables")
    @ApiOperation(value = "查询流程参数", httpMethod = "GET")
    public AjaxResponse variables(@RequestParam String instanceId) {

        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs(Constants.USER_BAJIE);
            }
            List<VariableInstance> variableInstance = processRuntime.variables(
                    ProcessPayloadBuilder.variables()
                            .withProcessInstanceId(instanceId)
                            .build());
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.ERROR.getDesc(),
                    variableInstance);

        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "查询流程实例参数失败", e.toString());
        }
    }

}
