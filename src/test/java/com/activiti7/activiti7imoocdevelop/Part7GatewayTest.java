package com.activiti7.activiti7imoocdevelop;

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

/**
 * 流程引擎网关
 *
 * @author debao.yang
 * @since 2022/7/20 17:29
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part7GatewayTest {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;

    /**
     * 部署流程图
     *
     * @author debao.yang
     * @since 2022/7/20 17:49
     */
    @Test
    public void deployBPMN() {
        String fileName = "BPMN/Part7_Parallel.bpmn20.xml";
        String processName = "并行网关";

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .name(processName)
                .deploy();
        System.out.println("流程实例部署成功，ID：" + deploy.getId());
    }

    /**
     * 创建流程定义以及流程实例
     *
     * @author debao.yang
     * @since 2022/7/20 17:50
     */
    @Test
    public void createProcessInstance() {
        String processDefinitionKey = "myProcess_Parallel";
        String businessKey = "bKey001";

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
                businessKey);
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());

    }


    @Test
    public void initProcessInstance() {
        String processDefinitionKey = "myProcess_Parallel";

        // myProcess_Parallel
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例的ID是：" + processInstance.getProcessInstanceId());

    }

    @Test
    public void completeTask() {
        // 18f27682-0812-11ed-ad13-b2c41870fafd
        String taskId = "a2aa9f6d-089a-11ed-be54-8257631e2066";
        taskService.complete(taskId);
        System.out.println("完成任务");
    }

    @Test
    public void getTasksByAssignee() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("tangseng")
                .taskAssignee("bajie")
                .list();
        taskList.forEach(item -> {
            System.out.println(item.getId());
            System.out.println(item.getName());
            System.out.println(item.getProcessInstanceId());
        });
    }

}
