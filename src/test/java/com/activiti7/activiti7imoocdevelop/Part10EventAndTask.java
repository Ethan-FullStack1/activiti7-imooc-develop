package com.activiti7.activiti7imoocdevelop;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 信号事件监听测试类
 *
 * @author debao.yang
 * @since 2024/6/11 12:25
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part10EventAndTask {
    private final RuntimeService runtimeService;

    @Test
    public void signalStart() {
        runtimeService.signalEventReceived("Signal_38ada5s");
    }

    @Test
    public void msgBack() {
        Execution execution = runtimeService.createExecutionQuery()
                                                 .messageEventSubscriptionName(
                                                         "Message_2qci7db")
                                                 .processInstanceId("ec9ce5c7-27af-11ef-b808-646ee0d6a055")
                                                 .singleResult();
        runtimeService.messageEventReceived("Message_2qci7db",
                execution.getId());
    }

}
