package com.activiti7.activiti7imoocdevelop.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TKListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行人1"+delegateTask.getAssignee());
        // 根据用户名查询用户电话并调用发短息接口。
        delegateTask.setVariable("delegateAssignee", delegateTask.getAssignee());
    }
}
