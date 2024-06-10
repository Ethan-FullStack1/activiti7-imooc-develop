package com.activiti7.activiti7imoocdevelop;

import com.activiti7.activiti7imoocdevelop.util.DateUtil;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 排他网关测试
 *
 * @author debao.yang
 * @since 2022/7/21 10:28
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part7ExclusiveGatewayTest {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    /**
     * 部署排他网关流程图
     *
     * @author debao.yang
     * @since 2022/7/21 10:30
     */
    @Test
    public void initDeployBPMN() {
        String fileName = "BPMN/Parti7_Exclusive.bpmn20.xml";
        Deployment deploy = repositoryService.createDeployment()
                .name("测试排他网关")
                .addClasspathResource(fileName)
                .deploy();
        System.out.println(deploy.getName());
    }

    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "myProcess_Exclusive";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getProcessInstanceId());
    }

    @Test
    public void taskList() {
        List<Task> list =
                taskService.createTaskQuery().taskAssignee("bajie").list();
        AtomicInteger integer = new AtomicInteger(1);
        list.forEach(item -> {
            System.out.println("开始输出第" + integer.get() + "个元素");
            System.out.println("ProcessInstanceId：" + item.getProcessInstanceId());
            System.out.println("Assignee：" + item.getAssignee());
            System.out.println("Name：" + item.getName());
            System.out.println("CreateTime：" + DateUtil.formatDefault(item.getCreateTime()));
            System.out.println("结束输出第" + integer.get() + "个元素\n\n");
            integer.getAndIncrement();
        });
    }

    @Test
    public void getTaskByAssignee() {
//        String asignee = "bajie";
        String asignee = "tangseng";

        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(asignee)
                .list();
        taskList.forEach(item -> {
            System.out.println("ID：" + item.getId());
            System.out.println("Name：" + item.getName());
            System.out.println("Assignee：" + item.getAssignee() + "\n\n");
        });
    }

    @Test
    public void completeTask() {
        // 在完成任务的时间添加流程变量
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("day", 100);

        String taskId = "0489b7e1-089f-11ed-a1cc-36b5a1547250";
        taskService.complete(taskId, variables);
        System.out.println("完成任务");
    }

}
