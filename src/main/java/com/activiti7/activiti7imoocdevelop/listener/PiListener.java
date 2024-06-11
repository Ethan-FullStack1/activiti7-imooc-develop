package com.activiti7.activiti7imoocdevelop.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

/**
 * @author debao.yang
 * @since 2024/6/10 21:42
 */
public class PiListener implements ExecutionListener {

    private Expression sendType;

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println(execution.getEventName());
        System.out.println(execution.getProcessDefinitionId());
        if ("start".equals(execution.getEventName())) {
            // 记录节点开始时间
        } else if ("end".equals(execution.getEventName())) {
            // 记录节点结束时间
        }
        System.out.println("snedType:" + sendType.getValue(execution));
    }
}
