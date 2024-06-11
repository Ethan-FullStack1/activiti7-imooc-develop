package com.activiti7.activiti7imoocdevelop.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 服务任务监听类
 *
 * @author debao.yang
 * @since 2024/6/11 15:33
 */
public class ServiceTaskListener2 implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Object aa = execution.getVariable("aa");
        System.out.println(aa);
    }
}
