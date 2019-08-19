package com.tedu.base.workflow.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: 获取指定人员、指定角色、指定部门主管、指定项目经理
 * @date 2017/11/20
 */
@Service("workflowUserService")
public interface WorkflowUserService {
    //根据用户名获取人员姓名
    List<String> getEmpNameByUserId(String userId);

    //获取角色相关人员
    List<String> getEmpNameByRoleId(String roleId);

    //获取岗位人员
    List<String> getEmpNameByPositionId(String postId);

    //获取项目经理
    List<String> getPmNameByProjId(String projId);

    //获取部门主管
    List<String> getManagerNameByOrgId(String orgId);


    //获取人名
    Map<String,String> getNamesByIds(List<String>ids );

    //获取当前人的部门主管
    List<String> getManagerNameByEmpId(String empId);

    List<String> getTestManagerByProjId(String projId);

    List<String> getProductManagerByProjId(String projId);
}
