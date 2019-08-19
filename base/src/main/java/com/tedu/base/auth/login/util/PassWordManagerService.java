package com.tedu.base.auth.login.util;

import com.tedu.base.engine.aspect.ILogicPlugin;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2018/2/26
 */
@Component ( "pwdManagerService" )
public class PassWordManagerService implements ILogicPlugin {
    @Resource
    LdapUtil ldapUtil;

    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        Map<String, Object> mapData = formModel.getData();
        String authTypeName = mapData.get("authType").toString();;
        String account = mapData.get("name").toString();
        String newPassword = mapData.get("updatePassword").toString();
        if (authTypeName.equals("ldap")) {
            ldapUtil.userChangePassword("Manager", account,"",newPassword);
        }
        return null;
    }

    @Override
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) { formModel.getData();
    }
}
