package com.ruoyi.common.wxPat;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName : WechatPayRequest
 * @Description :
 * @Author : WANGKE
 * @Date: 2023-08-26 02:27
 */
@Component
public class WechatPayRequest {
    @Resource
    private CloseableHttpClient wxPayClient;

    /**
     * 支付请求
     *
     * @param url
     * @param paramsStr
     * @return
     */
    public String wechatHttpOrderPost(String url, String paramsStr) {
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(paramsStr, "utf-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");

            CloseableHttpResponse response = wxPayClient.execute(httpPost);
            //响应体
            HttpEntity entity = response.getEntity();
            String body = entity == null ? "" : EntityUtils.toString(entity);
            //响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            //处理成功,204是，关闭订单时微信返回的正常状态码
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_NO_CONTENT) {
                System.out.println("成功, 返回结果 = " + body);
            } else {
                String msg = "微信支付请求失败,响应码 = " + statusCode + ",返回结果 = " + body;
                System.out.println("支付模块-生成订单 = " + msg);
                throw new RuntimeException(msg);
            }
            return body;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 退款请求
     *
     * @param url
     * @param paramsStr
     * @return
     */
    public String wechatHttpPost(String url, String paramsStr) {
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(paramsStr, "utf-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");

            CloseableHttpResponse response = wxPayClient.execute(httpPost);
            //响应体
            HttpEntity entity = response.getEntity();
            String body = entity == null ? "" : EntityUtils.toString(entity);
            //响应状态码
            int statusCode = response.getStatusLine().getStatusCode();

            //处理成功,204是，关闭订单时微信返回的正常状态码
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_NO_CONTENT) {
                System.out.println("成功, 返回结果 = " + body);
                // 请求成功或已处理成功，返回成功的响应
                return "退款处理中";
            } else if (statusCode == HttpStatus.SC_BAD_REQUEST || statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                // 请求参数错误或系统错误，返回失败的响应
                JSONObject json = JSONObject.parseObject(body);
                return json.getString("message");
            } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                // 权限问题，没有退款权限
                return "没有退款权限";
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                // 订单号不存在
                return "订单号不存在";
            } else if (statusCode == 429) {
                // 频率限制
                return "退款请求频率过高，请稍后重试";
            } else if (statusCode == HttpStatus.SC_PAYMENT_REQUIRED) {
                // 余额不足
                return "商户余额不足，请充值后重试";
            } else {
                // 其他状态码，返回通用的失败响应
                return "退款失败，请稍后重试";
            }
        } catch (Exception e) {
            System.out.println("支付模块-退款失败 = " + e.getMessage());
            JSONObject json = JSONObject.parseObject(e.getMessage());
            return json.getString("message");
        }
    }
}
