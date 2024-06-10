package com.activiti7.activiti7imoocdevelop;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Deployment test
 *
 * @author 多宝
 * @since 2022/7/17 13:09
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Part1DeploymentTest {

    private final RepositoryService repositoryService;

    /**
     * 测试初始化BPMN
     *
     * @author 多宝
     * @since 2022/7/17 13:17
     */
    @Test
    public void initDeploymentBPMN() {
        String filename = "BPMN/Part6_UEL_V3.bpmn20.xml";
//        String bpmnPicture = "BPMN/Part4_Task_Claim.png";

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
//                .addClasspathResource(bpmnPicture)
                .name("流程部署测试UELV3")
                .deploy();

        System.out.println(deployment.getName());
    }

    /**
     * 以ZIP的形式部署流程
     *
     * @author 多宝
     * @since 2022/7/17 13:41
     */
    @Test
    @SneakyThrows
    public void initDeploymentZIP() {
        @Cleanup InputStream fileInputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("BPMN/Part1_DeploymentV2.zip");
        assert fileInputStream != null;
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);

        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("流程部署测试ZIP")
                .deploy();

        System.out.println(deployment.getName());
    }

    /**
     * 获取流程部署实例
     *
     * @author 多宝
     * @since 2022/7/17 13:50
     */
    @Test
    public void getDeployments() {
        List<Deployment> list = repositoryService.createDeploymentQuery()
                .list();
        list.forEach(item -> {
            System.out.println(item.getId());
            System.out.println(item.getName());
            System.out.println(item.getDeploymentTime());
            System.out.println(item.getKey());
        });
    }

    @Test
    public void delDeployment() {
        String deploymentId = "62dfb666-06e1-11ed-bf6f-646ee0d6a052";
        repositoryService.deleteDeployment(deploymentId);
        System.out.println("删除流程实例成功");
    }

}
