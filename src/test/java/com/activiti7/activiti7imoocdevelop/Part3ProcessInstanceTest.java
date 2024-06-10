package com.activiti7.activiti7imoocdevelop;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 流程实例对象定义
 *
 * @author 多宝
 * @since 2022/7/17 19:45
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part3ProcessInstanceTest {

    private final RuntimeService runtimeService;

    /**
     * 初始化流程实例
     *
     * @author 多宝
     * @since 2022/7/17 19:46
     */
    @Test
    public void initProcessInstance() {
        // 1、获取页面表单填报的内容，请假时间，请假事由，Sting fromData
        // 2、fromData 写入业务表，返回业务表主键ID==businessKey
        // 3、把业务数据与Activiti7流程数据关联
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("myProcess_1uel_V2",
                                                         "bKey003");
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
    }

    /**
     * 获取流程实例
     *
     * @author 多宝
     * @since 2022/7/17 19:47
     */
    @Test
    public void getProcessInstances() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .list();
        if (CollectionUtils.isEmpty(processInstances)) {
            System.out.println("没有流程实例");
        } else {
            processInstances.forEach(item -> {
                System.out.println("----------流程实例-----------");
                System.out.println(item.getId());
                System.out.println(item.getProcessDefinitionId());
                System.out.println(item.isEnded());
                System.out.println(item.isSuspended());
            });
        }
    }

    /**
     * 激活与暂停流程实例
     *
     * @author 多宝
     * @since 2022/7/17 19:48
     */
    @Test
    public void activeProcessInstances() {

        String processInstanceId = "384509c2-05c8-11ed-ae33-646ee0d6a052";

        // 挂起流程实例
        // 如果是已经挂起的流程实例，再次挂起，是会报错的。
//        runtimeService.suspendProcessInstanceById(processInstanceId);
//        System.out.println("挂起流程实例");

        runtimeService.activateProcessInstanceById(processInstanceId);
        System.out.println("激活流程实例");

    }

    /**
     * 删除流程实例
     *
     * @author 多宝
     * @since 2022/7/17 19:49
     */
    @Test
    public void delProcessInstance() {
        String processInstanceId = "92bcc9a3-05c7-11ed-9233-646ee0d6a052";

        runtimeService.deleteProcessInstance(processInstanceId, "删着玩");
        System.out.println("删除流程实例");
    }


}
