package com.activiti7.activiti7imoocdevelop;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * UEL表达式测试类
 *
 * @author 多宝
 * @since 2022/7/19 4:43
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part6UELTest {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    /**
     * 启动流程实例带参数，指定执行人
     *
     * @author 多宝
     * @since 2022/7/19 4:45
     */
    @Test
    public void initProcessInstanceWithArgs() {
        // 流程变量  ${ZhiXingRen}
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("ZhiXingRen", "wukong");

        ProcessInstance instance =
                runtimeService.startProcessInstanceByKey("myProcess_UEL_V1",
                        "bKey002", variables);
        System.out.println("流程实例ID：" + instance.getProcessDefinitionId());
    }

    /**
     * 完成任务带参数，指定流程变量测试
     *
     * @author 多宝
     * @since 2022/7/19 4:47
     */
    @Test
    public void completeTaskWithArgs() {
        String taskId = "f1d9ac82-07db-11ed-8611-2e3c7bc0bbbe";

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("pay", "101");

        taskService.complete(taskId, variables);
        System.out.println("完成任务");
    }

    /**
     * 启动流程实例带参数，使用实体类
     *
     * @author 多宝
     * @since 2022/7/19 4:48
     */
    @Test
    public void initProcessInstanceWithClassArgs() {
        // myProcess_uelv3
        String processDefinitionKey = "myProcess_uelv3";
        String businessKey = "bKey002";

        UEL_POJO pojo = new UEL_POJO();
        pojo.setZhixingren("bajie");

        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("uelpojo", pojo);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
                businessKey, variables);
        System.out.println("流程实例ID" + processInstance.getProcessInstanceId());

    }

    /**
     * 任务完成环节带参数，指定多个候选人
     *
     * @author debao.yang
     * @since 2024/6/8 10:40
     */
    @Test
    public void initProcessInstanceWithCandiDateArgs() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("huoxuanren", "wukong,tangseng");
        taskService.complete("f1d9ac82-07db-11ed-8611-2e3c7bc0bbbe", variables);
        System.out.println("任务完成");
    }


    /**
     * 直接指定流程变量
     *
     * @author 多宝
     * @since 2022/7/19 4:52
     */
    @Test
    public void otherArgs() {
        String processInstanceId = "5fb6b7f6-07f7-11ed-9297-8202b7cd29a4";

        runtimeService.setVariable(processInstanceId, "pay", "101");
//         runtimeService.setVariables();
    }

    /**
     * 局部变量
     * <p>
     * 在局部变量之前做的所有的变量就是叫做全局变量。
     * 理想状态下它是覆盖，就是你之前key设置了一个101，后来这个key设置成了99。
     * 理想状态字啊使用的时间，这个key应该是99.
     * 但是在activiti7-M4版本他不是覆盖，它是保留了第一个版本。
     * <p>
     * 局部变量，顾名思义，他只在你当前这个环节设置的这个变量是有用的，过了这个环节，这个变量就没有用了。
     *
     * @author 多宝
     * @since 2022/7/19 4:53
     */
    @Test
    public void otherLocalArgs() {
        String processInstanceId = "5fb6b7f6-07f7-11ed-9297-8202b7cd29a4";

        runtimeService.setVariableLocal(processInstanceId, "pay", "101");
    }

    /**
     * 重点：
     *
     * 1、可以通过变量进行赋值，在启动的环节可以赋值，在任务完成的环节可以赋值，并且在任意的环节否是可以赋值的。
     * 2、通过实体类赋值的时间一定是要注意两点，一点是变量名。bpmn的assignee
     * 的变量名一定要是小写。如果是大小写混排，时会报大小写找不到的错误，并且实体类必须是需要序列化的。
     * 但是实际工作的话，不要用实体类进行书写变量，直接写成一个map，见名知意
     *
     *
     */


}
