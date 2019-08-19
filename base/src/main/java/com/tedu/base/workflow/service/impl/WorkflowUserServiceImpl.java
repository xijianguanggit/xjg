package com.tedu.base.workflow.service.impl;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.workflow.dao.WorkflowUserDao;
import com.tedu.base.workflow.service.WorkflowUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yangjixin
 * @Description: 获取流程图使用人员
 * @date 2017/11/20
 */
@Service("workflowUserService")
public class WorkflowUserServiceImpl implements WorkflowUserService {

    @Resource
    WorkflowUserDao workflowUserDao;


    @Override
    public List<String> getEmpNameByUserId(String userId) {
        if (userId.equals("")) {
            return Arrays.asList("");
        } else {
            return getUsers(workflowUserDao.getEmpNameByUserId(userId));
        }

    }

    @Override
    public List<String> getEmpNameByRoleId(String roleId) {
        if (roleId.equals("")) {
            return Arrays.asList("");
        } else {
            return getUsers(workflowUserDao.getEmpNameByRoleId(roleId));
        }
    }

    @Override
    public List<String> getEmpNameByPositionId(String postId) {
        if (postId.equals("")) {
            return null;
        } else {
            return getUsers(workflowUserDao.getEmpNameByPositionId(postId));
        }
    }

    @Override
    public List<String> getPmNameByProjId(String projId) {

        return getUsers(workflowUserDao.getPmNameByProjId(projId));
    }

    @Override
    public List<String> getManagerNameByOrgId(String orgId) {
        if (orgId.equals("")) {
            return null;
        } else {
            return getUsers(workflowUserDao.getManagerNameByOrgId(orgId));
        }
    }

    @Override
    public List<String> getManagerNameByEmpId(String empId) {

        return getUsers(workflowUserDao.getManagerNameByEmpId(empId));

    }

    @Override
    public List<String> getTestManagerByProjId(String projId) {
       return getUsers(workflowUserDao.getTestManagerByProjId(projId));
    }

    @Override
    public List<String> getProductManagerByProjId(String projId) {
      return  getUsers(workflowUserDao.getProductManagerByProjId(projId));
    }


    @Override
    public Map<String,String> getNamesByIds(List<String>ids) {
        List<UserModel> userModels = workflowUserDao.geNameByIds(ids);
        Map<String,String> map = new HashMap<String,String >();
        for(UserModel userModel : userModels){
          map.put(userModel.getUserId()+"",userModel.getEmpName());
        }
        return map;
    }


    private List<String> getUsers(List<UserModel> userModelList) {
        List<String> candidateUsers = new ArrayList<String>();
        for (int i = 0; i < userModelList.size(); i++) {
            UserModel userModel = userModelList.get(i);
            candidateUsers.add(userModel.getUserId()+"");
        }
        return candidateUsers;
    }

    private List<String> getEmails(List<UserModel> userModelList) {
        List<String> emails = new ArrayList<String>();
        for (int i = 0; i < userModelList.size(); i++) {
            UserModel userModel = userModelList.get(i);
            emails.add(userModel.getEmail());
        }
        return emails;
    }
}
