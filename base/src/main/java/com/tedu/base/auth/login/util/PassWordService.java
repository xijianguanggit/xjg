package com.tedu.base.auth.login.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.SessionUtils;
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
 * @date 2018/2/24
 */
@Component ( "pwdService" )
public class PassWordService implements ILogicPlugin {

    @Resource
    LdapUtil ldapUtil;

    @Override
    public Object doBefore(FormEngineRequest requestObj, FormModel formModel) {
        Map<String, Object> mapData = formModel.getData();
        String account = mapData.get("name").toString();
        String oldPassword = mapData.get("oldPassword").toString();
        String newPassword = mapData.get("updatePassword").toString();


        UserModel userInfo = SessionUtils.getUserInfo();
        String authTypeName  = userInfo.getAuthType();


        if (authTypeName.equals("ldap")) {
            if (ldapUtil.authenticate(account, oldPassword)) {
                ldapUtil.userChangePassword("", account, oldPassword, newPassword);
            } else {
                throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "输入的旧密码错误", "");
            }
        } else {
            String password = userInfo.getPassword();
            String salt = userInfo.getSalt();
            String validPwd = AviatorEvaluator.execute("Password('" + salt + "','" + oldPassword + "')").toString();
            //新旧密码匹配
            if (password.equals(validPwd)) {

            } else {
                throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA, "", "输入的旧密码错误");

            }
        }
        return null;
    }

    @Override
    public void doAfter(FormEngineRequest requestObj, FormModel formModel, Object beforeResult, FormEngineResponse responseObj) { formModel.getData();
    	SessionUtils.removeAll();
    }
}
