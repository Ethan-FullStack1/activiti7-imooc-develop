package com.activiti7.activiti7imoocdevelop;

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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 包含网关
 *
 * @author debao.yang
 * @since 2022/7/21 15:05
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part7InclusiveGatewayTest {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    /**
     * 部署流程
     *
     * @author debao.yang
     * @since 2022/7/21 15:06
     */
    @Test
    public void deployBPMN() {
        String path = "BPMN/Part7_Inclusive.bpmn20.xml";

        Deployment deploy = repositoryService.createDeployment()
                .name("包含网关部署")
                .addClasspathResource(path)
                .deploy();
        System.out.println(deploy.getName());
    }

    /**
     * 创建流程实例
     *
     * @author debao.yang
     * @since 2022/7/21 15:11
     */
    @Test
    public void createProcessInstance() {
        String processKey = "myProcess_Inclusive";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
    }

    /**
     * 查询当前人代办
     *
     * @author debao.yang
     * @since 2022/7/21 15:18
     */
    @Test
    public void getTaskByAssignee() {
//        String assignee = "bajie";
        String assignee = "wukong";

        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        taskList.forEach(item -> {
            System.out.println("ID：" + item.getId());
            System.out.println("Name：" + item.getName());
            System.out.println("Assignee：" + item.getAssignee());
        });
    }

    /**
     * 完成任务
     *
     * @author debao.yang
     * @since 2022/7/21 15:19
     */
    @Test
    public void completeTask() {
        // 进行变量的赋值
        HashMap<String, Object> variables = Maps.newHashMap();

        // 沙僧：d27fe546-08c5-11ed-b682-3a329a688aa6
        String ssTaskId = "d27fe546-08c5-11ed-b682-3a329a688aa6";
        // 悟空：d2800c58-08c5-11ed-b682-3a329a688aa6
        String wkTaskId = "d2800c58-08c5-11ed-b682-3a329a688aa6";
        // 流程实例ID：048746dd-089f-11ed-a1cc-36b5a1547250

        variables.put("day", 1);

//        String taskId = "b4876e1c-08c4-11ed-a52c-6603e45db713";
        taskService.complete(ssTaskId, variables);
        taskService.complete(wkTaskId, variables);
        System.out.println("完成任务");
    }

    /**
     * 获取所有流程实例
     *
     * @author debao.yang
     * @since 2022/7/21 15:25
     */
    @Test
    public void getInstances() {
        List<ProcessInstance> list =
                runtimeService.createProcessInstanceQuery()
                        .list();
        AtomicInteger integer = new AtomicInteger(1);
        list.forEach(item -> {
            System.out.println("开始输出第" + integer.get() + "流程实例信息");
            System.out.println("ProcessInstanceId：" + item.getProcessInstanceId());
            System.out.println("ProcessDefinitionId：" + item.getProcessDefinitionId());
            System.out.println("IsEnded：" + item.isEnded());
            System.out.println("IsSuspended：" + item.isSuspended());
            System.out.println("输出" + integer.get() + "条流程实例成功");
            integer.getAndIncrement();
            System.out.println("\n\n");
        });

    }

}
