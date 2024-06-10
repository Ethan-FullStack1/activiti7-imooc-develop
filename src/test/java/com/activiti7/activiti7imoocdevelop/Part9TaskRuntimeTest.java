package com.activiti7.activiti7imoocdevelop;

import com.activiti7.activiti7imoocdevelop.util.DateUtil;
import com.activiti7.activiti7imoocdevelop.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TaskRuntime测试类
 *
 * @author debao.yang
 * @since 2022/7/22 15:39
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part9TaskRuntimeTest {

    private final TaskRuntime taskRuntime;
    private final SecurityUtil securityUtil;

    /**
     * 获取当前登录用户任务
     *
     * @author debao.yang
     * @since 2022/7/22 15:41
     */
    @Test
    public void getTasksByCurrentUser() {
        securityUtil.logInAs("bajie");

        Page<Task> tasks = taskRuntime.tasks(Pageable.of(1, 100));
        List<Task> list = tasks.getContent();

        list.forEach(item -> {
            System.out.println(
                    "-------------------------------------------------");
            System.out.println("Id：" + item.getId());
            System.out.println("Name：" + item.getName());
            System.out.println("Status：" + item.getStatus());
            System.out.println("CreateDate：" + DateUtil.formatDefault(item.getCreatedDate()));
            if (StringUtils.isBlank(item.getAssignee())) {
                //    候选人为当前登录用户，null的时候需要前端拾取
                System.out.println("Assignee：待拾取任务");
            } else {
                // 否则执行人就是当前登录人
                System.out.println("Assignee：" + item.getAssignee());
            }
            System.out.println(
                    "--------------------------------------------------");
        });
    }

    /**
     * 完成任务
     *
     * @author debao.yang
     * @since 2022/7/22 15:41
     */
    @Test
    public void completeTask() {
        securityUtil.logInAs("bajie");

        String taskId = "f55fe566-067b-11ed-8ec8-debfb298f199";

        Task task = taskRuntime.task(taskId);
        if (StringUtils.isBlank(task.getAssignee())) {
            // 执行人为null的时间， 表示当前用户bajie其实是一个候选人，候选人就是需要先拾取任务。
            taskRuntime.claim(TaskPayloadBuilder.claim()
                    .withTaskId(task.getId())
                    .build());
        }
        taskRuntime.complete(TaskPayloadBuilder.complete()
                .withTaskId(task.getId())
                .build());
        System.out.println("任务执行完成");
    }

}
