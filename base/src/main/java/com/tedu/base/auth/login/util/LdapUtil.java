package com.tedu.base.auth.login.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tedu.base.engine.util.FormLogger;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/12/27
 */
@Component ( "ldapUtil" )
public class LdapUtil {

	public static final Logger log = Logger.getLogger(LdapUtil.class);
    private static final String LDAP_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    @Value ( "${url}" )
    private String LDAP_URL = "ldap://114.115.209.116:389/";

    @Value ( "${dn}" )
    private String DN = "cn=userName,ou=tsoftware,dc=tedu,dc=com";


    /**
     * 登陆认证
     *
     * @param account
     * @param password
     *
     * @return
     */
    public boolean authenticate(String account, String password) {
        if (account == null || "".equals(account)) {
            return false;
        }
        LdapContext ladpContent = connectLdap("", "");

        try {
            if (account.contains("Manager")) {

            } else {
                if (DN.equals("")) {
                    account = account + "@tarena.net";
                } else {
                    account = DN.replace("userName", account);
                }
            }
            ladpContent.addToEnvironment(Context.SECURITY_PRINCIPAL, account);
            ladpContent.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            ladpContent.reconnect(null);
        } catch (NamingException e) {
        	log.error("Ldap.authenticate error:"+e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * @param sUserName(Manager超级管理员账号)
     * @param oldPassword(Manager修改密码为空,自己修改为旧密码)
     * @param newPassword(自己修改新密码)
     * @param account(用户账号)
     *
     * @return
     */
    public boolean userChangePassword(String sUserName, String account, String oldPassword, String newPassword) {
        LdapContext ctx = connectLdap("", "");
        //不能从应用中修改超级管理员密码
        ModificationItem[] mods = new ModificationItem[1];
        if (sUserName != null && sUserName.equalsIgnoreCase("Manager")) {
            //管理员修改密码
            try {
                account = DN.replace("userName", account);
                ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=tedu,dc=com");
                ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, "1234");
                ctx.reconnect(null);
                //不验证他人密码
                String sshaPwd = generateSSHA(newPassword.getBytes());
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userpassword", sshaPwd));
                ctx.modifyAttributes(account, mods);
                return true;
            } catch (Exception e) {

            }

        } else {
            //自己修改自己的
            try {
                account = DN.replace("userName", account);
                ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, account);
                ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, oldPassword);
                ctx.reconnect(null);
                //String[] attrIDs = {"userpassword" };
                //Attributes pwd = ctx.getAttributes(account,attrIDs);
                //printAttrs(pwd);
                //System.out.println(sshaPwd);
                String sshaPwd = generateSSHA(newPassword.getBytes());
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userpassword", sshaPwd));
                ctx.modifyAttributes(account, mods);
                return true;
            } catch (Exception e) {
            	log.error(e.getMessage());
            } finally {
                try {
                    ctx.close();
                } catch (Exception e) {
                	log.error(e.getMessage());
                }
            }
            return false;
        }
        return true;
    }


    /**
     * 连接LDAP
     *
     * @param account
     * @param password
     *
     * @return
     */
    public LdapContext connectLdap(String account, String password) {
        Hashtable <String, String> env = getLdapEnvironmentConfig(account, password);

        LdapContext context = null;

        try {
            context = new InitialLdapContext(env, null);
        } catch (NamingException e) {
            // 连接失败日志打印
        	log.error("connectLdap error:"+e.getMessage());
        }

        return context;
    }

    /**
     * 获取LDAP环境配置
     *
     * @param account
     * @param password
     *
     * @return
     */
    private Hashtable <String, String> getLdapEnvironmentConfig(String account, String password) {
        Hashtable HashEnv = new Hashtable();
        HashEnv.put("java.naming.security.authentication", "simple");
        HashEnv.put("java.naming.security.principal", account);
        HashEnv.put("java.naming.security.credentials", password);
        HashEnv.put("java.naming.factory.initial", LDAP_FACTORY);
        HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");
        HashEnv.put("java.naming.provider.url", LDAP_URL);
        return HashEnv;
    }


    private static final int SALT_LENGTH = 4;

    public static String generateSSHA(byte[] password)
            throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password);
        crypt.update(salt);
        byte[] hash = crypt.digest();

        byte[] hashPlusSalt = new byte[hash.length + salt.length];
        System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
        System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

        return new StringBuilder().append("{SSHA}")
                .append(Base64.getEncoder().encodeToString(hashPlusSalt))
                .toString();
    }


    static void printAttrs(Attributes attrs) throws Exception {
        if (attrs == null) {
            System.out.println("No attributes");
        } else {
            /* Print each attribute */
            try {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMore(); ) {
                    Attribute attr = (Attribute) ae.next();
                    System.out.print(attr.getID() + " : ");
                    /* print each value */
                    if ("userpassword".equalsIgnoreCase(attr.getID())) {
                        for (
                                NamingEnumeration e = attr.getAll();
                                e.hasMore();
                                System.out.println(new String((byte[]) e.next()))
                                )
                            ;
                    } else {
                        for (
                                NamingEnumeration e = attr.getAll();
                                e.hasMore();
                                System.out.println(e.next())
                                )
                            ;
                    }
                }
            } catch (NamingException e) {
                FormLogger.error(e.getMessage(), e);
            }
        }
    }


    public static String hash(String password, String salt) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            String text = password;
            messageDigest.update(text.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
            for (byte b : digest)
                stringBuilder.append(String.format("%02x", b & 0xff));
            return stringBuilder.toString();
        } catch (Exception e) {

        }
        return "";
    }

}
