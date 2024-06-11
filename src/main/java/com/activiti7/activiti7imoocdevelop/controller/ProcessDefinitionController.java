package com.activiti7.activiti7imoocdevelop.controller;

import com.activiti7.activiti7imoocdevelop.enums.ResponseCode;
import com.activiti7.activiti7imoocdevelop.mapper.ActivitiMapper;
import com.activiti7.activiti7imoocdevelop.util.AjaxResponse;
import com.activiti7.activiti7imoocdevelop.util.DateUtil;
import com.activiti7.activiti7imoocdevelop.util.GlobalConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

/**
 * 流程定义接口
 *
 * @author debao.yang
 * @since 2022/7/27 16:04
 */
@RestController
@RequestMapping("/processDefinition")
@Api(value = "流程定义接口", tags = {"流程定义接口"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessDefinitionController {

    private final RepositoryService repositoryService;
    private final ActivitiMapper mapper;

    // 通过上传BPMN添加流程定义
    @PostMapping("uploadStreamAndDeployment")
    @ApiOperation(value = "通过上传BPMN添加流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processFile", value = "要上传的文件", dataType = "MultipartFile", required = true),
            /* @ApiImplicitParam(name = "processName", value = "流程名称", dataType =
                    "String", required = true) */
    })
    public AjaxResponse uploadStreamAndDeployment(
            @RequestParam MultipartFile processFile
           /*  @RequestParam String processName */
    ) {
        try {
            // 获取上传文件名
            String fileName = processFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(fileName);
            // 获取文件字节流对象
            InputStream fileInputStream = processFile.getInputStream();

            Deployment deployment = null;
            assert extension != null;
            if (extension.equals("zip")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()    // 初始化部署
                        .addZipInputStream(zip)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()
                        .addInputStream(fileName, fileInputStream)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            }

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(),
                    deployment.getId() + ";" + fileName);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "部署流程失败", e.toString());
        }
    }

    // 通过在线提交的BPMN的xml添加流程定义
    @PostMapping("/addDeploymentByString")
    @ApiOperation(value = "通过在线提交的BPMN的xml添加流程定义")
    public AjaxResponse addDeploymentByString(@RequestParam String stringBPMN
            // , @RequestParam String deploymentName
    ) {
        try {
            Deployment deploy = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                    .name("不知道在哪显示的部署名称")
                    .deploy();
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), deploy.getId());
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "string流程部署失败", e.toString());
        }
    }

    // 获取流程定义列表
    @GetMapping("getDefinitions")
    @ApiOperation(value = "获取流程定义列表", httpMethod = "GET")
    public AjaxResponse getDefinitions() {

        List<Map<String, Object>> listMap = Lists.newArrayList();

        try {
            List<ProcessDefinition> list =
                    repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionId().desc()
                    .list();
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(pd -> {
                    Map<String, Object> hashMap =
                            Maps.newHashMap();

                    hashMap.put("processDefinitionID", pd.getId());
                    hashMap.put("name", pd.getName());
                    hashMap.put("key", pd.getKey());
                    hashMap.put("resourceName", pd.getResourceName());
                    hashMap.put("deploymentID", pd.getDeploymentId());
                    hashMap.put("version", pd.getVersion());
                    listMap.add(hashMap);
                });
            }
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取流程定义失败", e.toString());
        }
    }

    // 获取流程定义xml
    @GetMapping("getDefinitionXML")
    @ApiOperation(value = "获取流程定义XML", httpMethod = "GET")
    public void getDefinitionXML(HttpServletResponse response,
                                 @RequestParam String deploymentId,
                                 @RequestParam String resourceName) {
        try {
            @Cleanup InputStream inputStream =
                    repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (-1 != inputStream.read(bytes)) {
                outputStream.write(bytes);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // 获取流程部署列表
    @GetMapping("getDeployments")
    @ApiOperation(value = "获取流程部署列表", httpMethod = "GET")
    public AjaxResponse getDeployments() {
        try {
            List<Map<String, Object>> listMap = Lists.newArrayList();

            List<Deployment> list = repositoryService.createDeploymentQuery()
                    .list();
            list.forEach(item -> {
                Map<String, Object> hashMap = Maps.newHashMap();

                hashMap.put("Id", item.getId());
                hashMap.put("Name", item.getName());
                hashMap.put("DeploymentTime",
                        DateUtil.formatDefault(item.getDeploymentTime()));

                listMap.add(hashMap);
            });

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "获取流程部署列表失败", e.toString());
        }
    }

    @GetMapping("/delDefinition")
    @ApiOperation(value = "删除流程定义", httpMethod = "GET")
    public AjaxResponse delDefinition(@RequestParam("depID") String depID, @RequestParam("pdID") String pdID) {
        try {

            //删除数据
            int result = mapper.DeleteFormData(pdID);

            repositoryService.deleteDeployment(depID, true);
            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    "删除成功", null);


        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "删除失败", e.toString());
        }
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", httpMethod = "POST")
    public AjaxResponse upload(HttpServletRequest request,
                               @RequestParam("processFile") MultipartFile processFile) {
        try {
            if (processFile.isEmpty()) {
                System.out.println("文件为空");
            }
            String filename = processFile.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));// 获取后缀名
            String filePath = GlobalConfig.BPMN_PathMapping.replace("file:", "");
            filename = UUID.randomUUID() + suffixName; // 新的文件名
            File file = new File(filePath + filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                processFile.transferTo(file);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            return AjaxResponse.ajaxData(ResponseCode.SUCCESS.getCode(),
                    ResponseCode.SUCCESS.getDesc(), filename);
        } catch (Exception e) {
            return AjaxResponse.ajaxData(ResponseCode.ERROR.getCode(),
                    "文件上传失败", e.toString());
        }
    }

}
