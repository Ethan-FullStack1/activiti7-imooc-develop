package com.activiti7.activiti7imoocdevelop;

import lombok.RequiredArgsConstructor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ProcessDefinition
 *
 * @author 多宝
 * @since 2022/7/17 14:06
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part2ProcessDefinitionTest {

    private final RepositoryService repositoryService;

    /**
     * 查询ProcessDefinition
     *
     * @author 多宝
     * @since 2022/7/17 14:08
     */
    @Test
    public void getDefinitions() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();
        AtomicInteger integer = new AtomicInteger(1);
        list.forEach(item -> {

            System.out.println("流程定义" + integer.get());
            System.out.println("Name:" + item.getName());
            System.out.println("Key:" + item.getKey());
            System.out.println("ResourceName:" + item.getResourceName());
            System.out.println("DeploymentId:" + item.getDeploymentId());
            System.out.println("Version:" + item.getVersion());
            integer.getAndIncrement();

            System.out.println();
            System.out.println();
            System.out.println();
        });

    }

    /**
     * 删除ProcessDefinition
     *
     * @author 多宝
     * @since 2022/7/17 14:10
     */
    @Test
    public void delDefinitions() {

        /*
            boolean cascade：
            true
            这个流程定义的删除将会删除他下面所有的流程实例，任务以及历史。就是说将会完全把历史痕迹完全删除掉。
            false
            会保留历史，就是把现在正在使用的流程实例给删除掉，但是已经执行完的历史任务是删除不掉的。
            这个要根据实际需要去做，一般来说，是需要选择false的。
            因为所有的做过的事情都要留痕。
            都要追责，false就是操作后算账。
            true就是一笔勾销。
         */
        String pdID = "5ff47854-0590-11ed-917a-646ee0d6a052";
//        String pdID = "1122";
        repositoryService.deleteDeployment(pdID, true);
        System.out.println("删除流程定义成功");


    }


}
