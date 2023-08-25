package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName : WxLawyerAppConfig
 * @Description : 微信配置文件
 * @Author : WANGKE
 * @Date: 2023-08-22 00:37
 */
@Component
@ConfigurationProperties(prefix = "wx-user-app")
public class WxUserAppConfig {
    /** 微信appId */
    private String appId;

    /** 微信appSecret */
    private String appSecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
