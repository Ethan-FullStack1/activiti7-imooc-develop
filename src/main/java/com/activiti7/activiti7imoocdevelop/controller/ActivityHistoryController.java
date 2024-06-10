package com.activiti7.activiti7imoocdevelop.controller;

import com.activiti7.activiti7imoocdevelop.enums.ResponseCode;
import com.activiti7.activiti7imoocdevelop.pojo.UserInfoBean;
import com.activiti7.activiti7imoocdevelop.util.AjaxResponse;
import com.activiti7.activiti7imoocdevelop.util.GlobalConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 查询历史接口
 *
 * @author debao.yang
 * @since 2022/10/17 16:31
 */
@RestController
@RequestMapping("/activitiHistory")
@Api(value = "查询历史接口", tags = {"查询历史接口"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityHistoryController {

    private final HistoryService historyService;
    private final RepositoryService repositoryService;

    // 用户历史任务
    @GetMapping("/getInstancesByUsername")
    @ApiOperation(value = "查询用户历史任务")
    public AjaxResponse getInstancesByUsername(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime()
                    .desc()
                    .taskAssignee(userInfoBean.getUsername())
                    .list();

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取用户历史任务失败", e.toString());
        }
    }

    // 根据流程实例ID查询任务
    @GetMapping("/getInstancesByPiID")
    @ApiOperation(value = "根据流程实例ID查询任务")
    public AjaxResponse getInstancesByPiID(@RequestParam String piID) {

        try {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime()
                    .desc()
                    .processInstanceId(piID)
                    .list();

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), list);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取历史任务失败", e.toString());
        }
    }

    // 高亮显示流程历史
    @GetMapping("gethighLine")
    @ApiOperation(value = "高亮显示历史流程", httpMethod = "GET")
    public AjaxResponse getHighLine(@RequestParam("instanceId") String instanceID,
                                    @ApiIgnore @AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceID)
                    .singleResult();
            // 读取BPMN
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);
            // 获取所有流程FlowElement的信息
            Collection<FlowElement> flowElements = process.getFlowElements();
            Map<String, String> map = Maps.newHashMap();
            flowElements.forEach(flowElement -> {
                // 判断是否是线条
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef, flowElement.getId());
                }
            });

            // 获取全部历史流程节点
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .list();
            // 各历史节点两两组合成Key
            Set<String> keyList = Sets.newHashSet();
            list.forEach(i -> {
                list.forEach(j -> {
                    if (i != j) {
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                });
            });

            // 高亮连线ID
            Set<String> highLine = Sets.newHashSet();
            keyList.forEach(item -> highLine.add(map.get(item)));

            // 获取已经完成的节点
            List<HistoricActivityInstance> hasFinishedNode = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .finished()
                    .list();
            // 已经完成的节点高亮
            Set<String> highPoint = Sets.newHashSet();
            hasFinishedNode.forEach(item -> highPoint.add(item.getActivityId()));

            // 获取待办节点
            List<HistoricActivityInstance> hasNotFinishedNode = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .unfinished()
                    .list();
            // 待办高亮节点
            Set<String> waiitngTodo = Sets.newHashSet();
            hasNotFinishedNode.forEach(item -> waiitngTodo.add(item.getActivityId()));

            // 当前用户完后的任务
            String assigneeName = null;
            if (GlobalConfig.Test) {
                assigneeName = "bajie";
            } else {
                assigneeName = userInfoBean.getUsername();
            }

            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(assigneeName)
                    .processInstanceId(instanceID)
                    .finished()
                    .list();
            Set<String> iDone = Sets.newHashSet();
            taskInstanceList.forEach(item -> iDone.add(item.getTaskDefinitionKey()));

            Map<String, Object> reMap = Maps.newHashMap();
            reMap.put("highLine", highLine);
            reMap.put("highPoint", highPoint);
            reMap.put("waiitngToDo", waiitngTodo);
            reMap.put("iDo", iDone);

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), reMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "高亮历史任务失败", e.toString());
        }
    }

}
