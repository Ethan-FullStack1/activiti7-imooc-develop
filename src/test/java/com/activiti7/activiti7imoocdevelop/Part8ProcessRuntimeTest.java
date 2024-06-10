package com.activiti7.activiti7imoocdevelop;

import com.activiti7.activiti7imoocdevelop.util.DateUtil;
import com.activiti7.activiti7imoocdevelop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ProcessRuntimeTest
 * <p>
 * 使用Activiti7的新特性实现之前实现的操作。
 * <p>
 * 使用Activiti7的新的api实现
 *
 * @author debao.yang
 * @since 2022/7/22 09:44
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part8ProcessRuntimeTest {

    private final ProcessRuntime processRuntime;
    private final SecurityUtil securityUtil;
    private final RepositoryService repositoryService;
    private final TaskService taskService;


    /**
     * 获取流程实例
     *
     * @author debao.yang
     * @since 2022/7/22 09:46
     */
    @Test
    public void initProcessInstance() {
        securityUtil.logInAs("bajie");
        Page<ProcessInstance> instances = processRuntime.processInstances(Pageable.of(0, 100));
        System.out.println("流程实例的总数" + instances.getTotalItems());
        List<ProcessInstance> content = instances.getContent();
        content.forEach(item -> {
            System.out.println("--------------------------------");
            System.out.println("Id：" + item.getId());
            System.out.println("Name：" + item.getName());
            System.out.println("StartDate：" + DateUtil.formatDefault(item.getStartDate()));
            System.out.println("Status：" + item.getStatus());
            System.out.println("ProcessDefinationId：" + item.getProcessDefinitionId());
            System.out.println("ProcessDefinitionKey：" + item.getProcessDefinitionKey());
            System.out.println("------------------------------------- ");
        });
    }

    /**
     * 部署BPMN
     *
     * @author debao.yang
     * @since 2022/7/22 10:19
     */
    @Test
    public void deployBPMN() {
        String fileName = "BPMN/Part8_ProcessRuntime.bpmn20.xml";
        Deployment deploy = repositoryService.createDeployment()
                .name("测试部署ProcessRuntime")
                .addClasspathResource(fileName)
                .deploy();
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程实例
     *
     * @author debao.yang
     * @since 2022/7/22 09:48
     */
    @Test
    public void startProcessInstance() {
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("myProcess_ProcessRuntime")
                .withName("第一个流程实例名称")
                // .withVariable()
                .withBusinessKey("自定义bKey")
                .build());
    }

    @Test
    public void getTasksByAssignee() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("bajie")
                .orderByTaskCreateTime()
                .desc()
                .list();
        taskList.forEach(item -> {
            System.out.println("Id：" + item.getId());
            System.out.println("ProcessInstanceId：" + item.getProcessInstanceId());
            System.out.println("Name；" + item.getName());
            System.out.println("Status：" + item.getDelegationState());
            System.out.println("TaskStartTime：" + DateUtil.formatDefault(item.getCreateTime()) + "\n" +
                    "\n");
        });
    }

    /**
     * 删除流程实例
     *
     * @author debao.yang
     * @since 2022/7/22 09:49
     */
    @Test
    public void delProcessInstance() {
        String processInstanceId = "69108354-0965-11ed-b804-9224ec3b4243";

        securityUtil.logInAs("bajie");
        ProcessInstance instance = processRuntime.delete(ProcessPayloadBuilder.delete()
                .withProcessInstanceId(processInstanceId)
                .build());
    }

    /**
     * 挂起流程实例
     *
     * @author debao.yang
     * @since 2022/7/22 09:50
     */
    @Test
    public void suspendProcessInstance() {
        securityUtil.logInAs("bajie");
        String processInstanceId = "048746dd-089f-11ed-a1cc-36b5a1547250";

        processRuntime.suspend(ProcessPayloadBuilder.suspend()
                .withProcessInstanceId(processInstanceId)
                .build());
    }

    /**
     * 激活流程实例
     *
     * @author debao.yang
     * @since 2022/7/22 09:51
     */
    @Test
    public void resumeProcessInstance() {
        securityUtil.logInAs("bajie");
        String processInstanceId = "048746dd-089f-11ed-a1cc-36b5a1547250 ";

        processRuntime.resume(ProcessPayloadBuilder.resume()
                .withProcessInstanceId(processInstanceId)
                .build());
    }

    /**
     * 获取流程实例的参数
     *
     * @author debao.yang
     * @since 2022/7/22 09:52
     */
    @Test
    public void getProcessInstanceVariables() {
        securityUtil.logInAs("bajie");
        String processInstanceId = "048746dd-089f-11ed-a1cc-36b5a1547250 ";

        List<VariableInstance> variables = processRuntime.variables(ProcessPayloadBuilder.variables()
                .withProcessInstanceId(processInstanceId)
                .build());
        variables.forEach(item -> {
            System.out.println("Name：" + item.getName());
            System.out.println("Type：" + item.getType());
            System.out.println("Value：" + item.getValue());
        });
    }


}
