package com.activiti7.activiti7imoocdevelop;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 流程历史记录测试
 *
 * @author 多宝
 * @since 2022/7/19 3:55
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part5HistoricTaskInstanceTest {

    private final HistoryService historyService;

    /**
     * 根据用户名查询历史记录
     *
     * @author 多宝
     * @since 2022/7/19 3:57
     */
    @Test
    public void getHistoryByUser() {
        List<HistoricTaskInstance> historicTaskInstances =
                historyService.createHistoricTaskInstanceQuery()
                              .orderByHistoricTaskInstanceEndTime().asc()
                              .taskAssignee("bajie")
                              .list();
        historicTaskInstances.forEach(item -> {
            System.out.println("Id：" + item.getId());
            System.out.println(
                    "ProcessInstanceId：" + item.getProcessInstanceId());
            System.out.println("Name：" + item.getName());
            System.out.println("Assignee：" + item.getAssignee());
        });
    }

    /**
     * 根据流程实例ID查询历史
     *
     * @author 多宝
     * @since 2022/7/19 3:58
     */
    @Test
    public void getHistoryByID() {
        String processInstanceId = "028a3fa0-05d9-11ed-8af8-646ee0d6a052";

        List<HistoricTaskInstance> historicTaskInstances =
                historyService.createHistoricTaskInstanceQuery()
                              .orderByHistoricTaskInstanceEndTime().asc()
                              .processInstanceId(processInstanceId)
                              .list();
        historicTaskInstances.forEach(item -> {
            System.out.println("Id：" + item.getId());
            System.out.println(
                    "ProcessInstanceId：" + item.getProcessInstanceId());
            System.out.println("Name：" + item.getName());
            System.out.println("Assignee：" + item.getAssignee());
        });
    }


}
