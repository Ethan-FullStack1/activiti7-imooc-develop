package com.activiti7.activiti7imoocdevelop;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 流程执行人、候选人、候选组测试
 *
 * @author 多宝
 * @since 2022/7/17 20:56
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part4TaskTest {

    private final TaskService taskService;


    /**
     * 任务查询
     *
     * @author 多宝
     * @since 2022/7/17 20:57
     */
    @Test
    public void getTasks() {

        List<Task> list = taskService.createTaskQuery().list();
        list.forEach(item -> {
            System.out.println("Id：" + item.getId());
            System.out.println("Name：" + item.getName());
            System.out.println("Assignee：" + item.getAssignee());

        });

    }

    /**
     * 查询我的代办任务
     *
     * @author 多宝
     * @since 2022/7/17 20:58
     */
    @Test
    public void getTasksByAssignee() {
        List<Task> list =
                taskService.createTaskQuery()
                        .taskAssignee("bajie")
//                        .taskCandidateUser()
//                        这个是查询任务候选人的api，由于activiti7的新特性，这里测试类会告诉你说用户未登录，会报错。
                        .list();
        if (CollectionUtils.isEmpty(list)) {
            System.out.println("当前人没有待办");
        } else {
            list.forEach(item -> {
                System.out.println("Id：" + item.getId());
                System.out.println("Name：" + item.getName());
                System.out.println("Assignee：" + item.getAssignee());
            });
        }

    }

    /**
     * 执行任务
     *
     * @author 多宝
     * @since 2022/7/17 20:59
     */
    @Test
    public void completeTask() {
        String taskId = "ba442178-0679-11ed-aff3-debfb298f199";
        taskService.complete(taskId);
        System.out.println("执行任务");

    }

    /**
     * 拾取任务
     *
     * @author 多宝
     * @since 2022/7/17 21:00
     */
    @Test
    public void claimTask() {
        // 1、首先先把任务查询出来
        String taskId = "f55fe566-067b-11ed-8ec8-debfb298f199";
        Task task =
                taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2、由八戒拾取了当前这个任务
        taskService.claim(taskId, "bajie");
    }

    /**
     * 归还与交办任务
     *
     * @author 多宝
     * @since 2022/7/19 3:47
     */
    @Test
    public void setTaskAssignee() {
        String taskId = "028a3fa0-05d9-11ed-8af8-646ee0d6a052";

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 这个其实就是设置任务的执行人
        taskService.setAssignee(taskId, "null"); // 归还候选任务
        taskService.setAssignee(taskId, "wukong"); // 交办

    }

}
