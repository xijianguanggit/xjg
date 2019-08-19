package com.tedu.base.workflow.util;

import com.tedu.base.engine.util.FormLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/11/7
 */
public class WorkflowDeploy implements InitializingBean,
        ApplicationContextAware {
    private Resource[] deploymentResources;
    private String category;
    ApplicationContext appCtx;

    @Value("${base.bpmnDeploy.startUp}")
    private String startUp;

    public void setDeploymentResources(Resource[] resources) {
        this.deploymentResources = resources;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.appCtx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (category == null) {
            throw new FatalBeanException("缺失属性 : category");
        }



        if (startUp.equals("1")&&deploymentResources != null) {
            RepositoryService repositoryService = appCtx
                    .getBean(RepositoryService.class);
            for (Resource r : deploymentResources) {
                String deploymentName = category + "_" + r.getFilename();
                String resourceName = r.getFilename();
                boolean doDeploy = true;
                List<Deployment> deployments = repositoryService
                        .createDeploymentQuery().deploymentName(deploymentName)
                        .orderByDeploymenTime().desc().list();
                if (!deployments.isEmpty()) {
                    Deployment existing = deployments.get(0);
                    try {
                        InputStream in = repositoryService.getResourceAsStream(
                                existing.getId(), resourceName);


                        if (in != null) {
                            File f = File.createTempFile(
                                    "deployment",
                                    ".bpmn",
                                    new File(System.getProperty("java.io.tmpdir")));
                            f.deleteOnExit();
                            OutputStream out = new FileOutputStream(f);
                            IOUtils.copy(in, out);
                            in.close();
                            out.close();
                            doDeploy = (FileUtils.checksumCRC32(f) != FileUtils
                                    .checksumCRC32(r.getFile()));


                        } else
                            throw new ActivitiException("不能读取资源 "
                                    + resourceName + ", 输入流为空");
                    } catch (ActivitiException ex) {

                    }
                }
                if (doDeploy) {

                    repositoryService.createDeployment().name(deploymentName)
                            .addInputStream(resourceName, r.getInputStream())
                            .deploy();
                    FormLogger.info("文件部署成功 : " + r.getFilename());
                }
            }
        }
    }
}
