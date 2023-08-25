package com.ruoyi.system.domain;

/**
 * @ClassName : WxLoginBody
 * @Description : 微信登录
 * @Author : WANGKE
 * @Date: 2023-08-22 00:35
 */
public class WxLoginBody {
    /**
     * 暂时登陆凭据 code 只能运用一次
     */
    private String loginCode;
    private String phoneCode;
    /**
     * 偏移量
     */
    private String encryptedIv;
    /**
     * 加密数据
     */
    private String encryptedData;

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getEncryptedIv() {
        return encryptedIv;
    }

    public void setEncryptedIv(String encryptedIv) {
        this.encryptedIv = encryptedIv;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }
}
