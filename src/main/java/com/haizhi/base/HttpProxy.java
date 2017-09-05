package com.haizhi.base;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class HttpProxy implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3569393980465582761L;
    private String host;// 代理主机ip
    private int port;// 代理主机端口
    private int type;// 0.http 1.Represents a SOCKS (V4 or V5) proxy.
    private String userName;// 代理账户
    private String passWord;// 代理密码

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String toStringUserAndpwd() {
        return userName + ":" + passWord + "@" + host + ":" + port;
    }

    public String toString() {
        return host + ":" + port;
    }

    /**
     * haizhi:haizhi@119.186.237.209:8888 根据给的代理字符串构造一个代理对象
     */
    public static HttpProxy build(String httpProxyStr) {
        String[] temp = StringUtils.split(httpProxyStr, "@");
        String[] userNameAndPwd = StringUtils.split(temp[0], ":");
        String[] hostAndPort = StringUtils.split(temp[1], ":");
        HttpProxy httpProxy = new HttpProxy();
        httpProxy.setHost(hostAndPort[0]);
        httpProxy.setPort(Integer.valueOf(hostAndPort[1]));
        httpProxy.setUserName(userNameAndPwd[0]);
        httpProxy.setPassWord(userNameAndPwd[1]);
        return httpProxy;
    }
}
