package com.tedu.base.workflow.dao;

import com.tedu.base.auth.login.model.UserModel;

import java.util.List;

/**
 * @author yangjixin
 * @Description: 流程图人员预制方法
 * @date 2017/11/20
 */
public interface WorkflowUserDao {
    //根据用户名获取人员姓名
    List<UserModel> getEmpNameByUserId(String userId);

    //获取角色相关人员
    List<UserModel> getEmpNameByRoleId(String roleId);

    //获取岗位人员
    List<UserModel> getEmpNameByPositionId(String postId);

    //获取项目经理
    List<UserModel> getPmNameByProjId(String projId);
    //获取部门主管
    List<UserModel> getManagerNameByOrgId(String orgId);

    //根据userId获取人名
    List<UserModel> geNameByIds(List<String>ids);

    //获取当前发起人的部门主管
    List<UserModel> getManagerNameByEmpId(String empId);


    List<UserModel> getTestManagerByProjId(String projId);

    List<UserModel> getProductManagerByProjId(String projId);
}
