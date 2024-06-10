package com.activiti7.activiti7imoocdevelop.controller;

import com.activiti7.activiti7imoocdevelop.common.Constants;
import com.activiti7.activiti7imoocdevelop.enums.ResponseCode;
import com.activiti7.activiti7imoocdevelop.mapper.ActivitiMapper;
import com.activiti7.activiti7imoocdevelop.util.AjaxResponse;
import com.activiti7.activiti7imoocdevelop.util.DateUtil;
import com.activiti7.activiti7imoocdevelop.util.GlobalConfig;
import com.activiti7.activiti7imoocdevelop.util.SecurityUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务接口
 *
 * @author 多宝
 * @since 2022/7/30 15:40
 */
@RestController
@RequestMapping("/task")
@Api(value = "任务接口", tags = {"任务接口"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {

    private final SecurityUtil securityUtil;
    private final TaskRuntime taskRuntime;
    private final ProcessRuntime processRuntime;
    private final RepositoryService repositoryService;
    private final ActivitiMapper activitiMapper;

    // 获取我的代办任务
    @GetMapping("/todoList")
    @ApiOperation(value = "查询我的代办", httpMethod = "GET")
    public AjaxResponse todoList(/* @RequestParam String username */) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }

            Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100));
            List<Map<String, Object>> mapList = Lists.newArrayList();

            tasks.getContent().forEach(item -> {
                Map<String, Object> hashMap = Maps.newHashMap();
                hashMap.put("id", item.getId());
                hashMap.put("name", item.getName());
                hashMap.put("status", item.getStatus());
                hashMap.put("createdDate",
                        DateUtil.formatDefault(item.getCreatedDate()));

                if (StringUtils.isEmpty(item.getAssignee())) {
                    hashMap.put("assignee", "待拾取任务");
                } else {
                    hashMap.put("assignee", item.getAssignee());
                }

                // 获取流程实例的名称
                ProcessInstance processInstance =
                        processRuntime.processInstance(
                                item.getProcessInstanceId());
                hashMap.put("instanceName", processInstance.getName());

                mapList.add(hashMap);
            });

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    mapList);

        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取我的待办任务失败", e.toString());
        }
    }

    // 完成任务
    @GetMapping(value = "/completeTask")
    @ApiOperation(value = "完成任务")
    public AjaxResponse complete(@RequestParam String taskID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskID);
            // 如果执行人为空的话，意思就是说当前登录用户不时执行人，而是候选人。
            if (task.getAssignee() == null) {
                // 候选人拾取任务
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(taskID)
                        .build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete()
                    .withTaskId(taskID)
                    //.withVariable("remarks", "完成了哇")
                    // 这个操作的意思就是说，在完成任务的时间是可以加参数的
                    .build());

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "完成任务：" + taskID + "失败", e.toString());
        }
    }

    // 渲染动态表单
    @GetMapping("/formDataShow")
    @ApiOperation(value = "渲染动态表单", httpMethod = "GET")
    public AjaxResponse formDataShow(@RequestParam("taskID") String taskId) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskId);
            // 构建表单控件历史数据字典
            Map<String, String> controlListMap = Maps.newHashMap();
            List<HashMap<String, Object>> tempControlList =
                    activitiMapper.selectFormData(task.getProcessInstanceId());
            for (HashMap<String, Object> ls : tempControlList) {
                controlListMap.put(ls.get("Control_ID_").toString(), ls.get(
                        "Control_VALUE_").toString());
            }

            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());
            if (userTask == null) {
                return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                        ResponseCode.SUCCESS.getDesc(), "无表单");
            }

            List<Map<String, Object>> mapList = new ArrayList<>();
            List<FormProperty> formProperties = userTask.getFormProperties();
            for (FormProperty fp : formProperties) {
                String[] splitFP = fp.getId()
                        .split(Constants.DYNAMIC_FORM_ID_DELIMITER);
                Map<String, Object> hashMap = Maps.newHashMap();
                hashMap.put("id", splitFP[0]);
                hashMap.put("controlType", splitFP[1]);
                hashMap.put("controlLable", splitFP[2]);

                // 如果默认值是表单控件ID
                if (splitFP[3].startsWith("FormProperty_")) {
                    if (controlListMap.containsKey(splitFP[3])) {
                        hashMap.put(
                                "controlDefValue",
                                controlListMap.get(splitFP[3]));
                    } else {
                        hashMap.put("controlDefValue",
                                "读取失败，检查" + splitFP[0] + "配置");
                    }
                } else {
                    hashMap.put(
                            "controlDefValue",
                            splitFP[3]);
                }

                hashMap.put("controlIsParam", splitFP[4]);
                mapList.add(hashMap);
            }

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), mapList);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "表单渲染失败", e.toString());
        }
    }

    // 保存动态表单
    @PostMapping("formDataSave")
    @ApiOperation(value = "保存动态表单")
    public AjaxResponse formDataSave(@RequestParam("taskID") String TaskID,
                                     @RequestParam("formData") String formData) {
        try {
            Task task = taskRuntime.task(TaskID);

            Map<String, Object> variables = Maps.newHashMap();
            boolean hasVariables = false;

            List<Map<String, Object>> listMap = Lists.newArrayList();
            // 前端传来的字符串拆分为控件组
            String[] formDataList = formData.split(Constants.SPLIT_CONTROL);
            for (String item : formDataList) {
                String[] formDataItem =
                        item.split(Constants.DYNAMIC_FORM_ID_DELIMITER);
                Map<String, Object> hashMap = Maps.newHashMap();
                hashMap.put("PROC_DEF_ID_", task.getProcessDefinitionId());
                hashMap.put("PROC_INST_ID_", task.getProcessInstanceId());
                hashMap.put("FORM_KEY_", task.getFormKey());
                hashMap.put("Control_ID_", formDataItem[0]);
                hashMap.put("Control_VALUE_", formDataItem[1]);
                listMap.add(hashMap);

                // 构建参数集合
                switch (formDataItem[2]) {
                    case "f":
                        System.out.println("控件值不作为参数");
                        break;
                    case "s":
                        variables.put(formDataItem[0], formDataItem[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        variables.put(formDataItem[0],
                                DateUtil.stringToDate(formDataItem[1],
                                        Constants.DEFAULT_FORMATER_WITHOUT_SECOND));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(formDataItem[0],
                                BooleanUtils.toBoolean(formDataItem[1]));
                        hasVariables = true;
                        break;
                    default:
                        System.out.println("控件ID：" + formDataItem[0] + "的参数" + formDataItem[2] + "不存在");
                }
            }
            if (hasVariables) {
                // 带参数完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete()
                        .withTaskId(TaskID)
                        .withVariables(variables)
                        .build());
            } else {
                taskRuntime.complete(TaskPayloadBuilder.complete()
                        .withTaskId(TaskID)
                        .build());
            }
            // 保存动态表单数据到数据库
            activitiMapper.insertFormData(listMap);

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), listMap);

        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "任务表单保存失败", e.toString());
        }


    }
}