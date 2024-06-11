package com.activiti7.activiti7imoocdevelop.listener;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 错误事件监听类
 *
 * @author debao.yang
 * @since 2024/6/11 13:35
 */
public class ErrServerTaskListener implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        throw new BpmnError("Error_0ggfaov");
    }
}
