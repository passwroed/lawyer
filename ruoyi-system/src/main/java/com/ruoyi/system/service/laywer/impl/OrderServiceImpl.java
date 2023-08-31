package com.ruoyi.system.service.laywer.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.config.WxLawyerAppConfig;
import com.ruoyi.common.config.WxUserAppConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.wxPat.WechatPayConfig;
import com.ruoyi.common.wxPat.WechatPayRequest;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.mapper.lawyer.OrderMapper;
import com.ruoyi.system.service.laywer.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * @ClassName : OrderServiceImpl
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-02 00:54
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Resource
    private WechatPayConfig wechatPayConfig;


    @Resource
    private WechatPayRequest wechatPayRequest;
    @Override
    public List<Order> list(Order order) {
        if (StringUtils.isNotNull(order.getPageNum()) && StringUtils.isNotNull(order.getPageSize())) {
            PageHelper.startPage(order.getPageNum(), order.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return orderMapper.list(order);
    }

    @Override
    public Order item(Long id) {
        return orderMapper.item(id);
    }

    @Override
    public Order itemNo(String no) {
        return orderMapper.itemNo(no);
    }

    @Override
    public Map add(Order order,String appid) {

        if (orderMapper.add(order) == 0){
            return null;
        }

        return payWxMap(order,appid);
    }

    @Override
    public int edit(Order order) {
        return orderMapper.edit(order);
    }

    @Override
    public int del(Long id) {
        return orderMapper.del(id);
    }


    @Override
    public Map refund(Order order) {
        refundOrder(order);
        return null;
    }
    private Map payWxMap(Order order,String appid){
        // 统一参数封装
        Map<String, Object> params = new HashMap<>(10);
        // 1,appid：公众号或移动应用的唯一标识符。
        params.put("appid", appid);
        // 2,mch_id：商户号，由微信支付分配。
        params.put("mchid", wechatPayConfig.getMchId());
        // 3.description body：商品描述。
        params.put("description", order.getGoodsName());
        int outRefundNo = new Random().nextInt(999999999);
        // 4.out_trade_no：商户订单号，由商户自定义。
        params.put("out_trade_no", order.getNo());
        // 5.notify_url：接收微信支付异步通知回调地址。
        params.put("notify_url", wechatPayConfig.getNotifyUrl());
        // 6.total_fee：订单总金额，单位为分。
        Map<String, Object> amountMap = new HashMap<>(4);
        // 金额单位为分
        amountMap.put("total", (int)(order.getMoney()*100));
        amountMap.put("currency", "CNY");
        params.put("amount", amountMap);


        // 7.openid：用户在商户appid下的唯一标识。
        Map<String, Object> payerMap = new HashMap<>(4);
        // openid  需要前端小程序通过用户code 请求微信接口获取用户唯一openid  不懂的看官方文档：https://developers.weixin.qq.com/doc/aispeech/miniprogram/quickuse.html
        payerMap.put("openid", getLoginUser().getOpenId());
        params.put("payer", payerMap);

        String paramsStr = JSON.toJSONString(params);
        System.out.println("请求参数 ===> {}" + paramsStr);

        // 微信预支付下单接口路径
        String payUrl = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
        // 获取支付 prepay_id参数
        String resStr = wechatPayRequest.wechatHttpOrderPost(payUrl, paramsStr);


        Map<String, Object> resMap = JSONObject.parseObject(resStr, new TypeReference<Map<String, Object>>() {
        });
        Object prepayId = resMap.get("prepay_id");
        String nonceStr = String.valueOf(new Random().nextInt(999999999));

        // 获取签名
        String paySign;
        try {
            StringBuilder sb = new StringBuilder();
            // 应用id
            sb.append(appid).append("\n");
            // 支付签名时间戳
            sb.append(System.currentTimeMillis() / 1000).append("\n");
            // 随机字符串
            sb.append(nonceStr).append("\n");
            // 预支付交易会话ID  这个要注意 key = "prepay_id=xxxxxx"
            sb.append("prepay_id=").append(prepayId).append("\n");
            // 签名
            Signature sign = Signature.getInstance("SHA256withRSA");
            // 获取商户私钥并进行签名
            PrivateKey privateKey = wechatPayConfig.getPrivateKey(wechatPayConfig.getKeyPemPath());
            sign.initSign(privateKey);
            sign.update(sb.toString().getBytes(StandardCharsets.UTF_8));
            // 得到签名
            paySign = Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            System.out.println("支付模块_生成交易签名失败！" + e);
            return null;
        }


        // 将签名时数据和签名一起返回前端用于前端吊起支付
        Map<String, Object> map = new HashMap<>();
        // 小程序id
        map.put("appId", appid);
        // 时间戳
        map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 随机字符串
        map.put("nonceStr", nonceStr);
        // 预支付交易会话ID
        map.put("package", "prepay_id=" + prepayId);
        // 签名方式
        map.put("signType", "RSA");
        // 签名
        map.put("paySign", paySign);
        return map;
    }
    public void refundOrder(Order order) {

        // 退款请求路径
        String url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
        // 设置参数
        Map<String, Object> params = new HashMap<>(2);
        // 要退款的订单编号订单编号
        params.put("out_trade_no", order.getNo());
        // 商户自定义退款记录单号 用于退款记录的单号 跟退款订单号不是一样的
        int outRefundNo = new Random().nextInt(999999999);
        System.out.println("退款申请号：{"+outRefundNo+"}");
        params.put("out_refund_no", outRefundNo + "");
        // 退款原因
        params.put("reason", order.getOrderLog().getReason());
        // 退款通知回调地址
        params.put("notify_url", wechatPayConfig.getRefundNotifyUrl());

        Map<String, Object> amountMap = new HashMap<>();
        //退款金额，单位：分
        amountMap.put("refund", (int)(order.getMoney()*100));
        //原订单金额，单位：分
        amountMap.put("total", (int)(order.getMoney()*100));
        //退款币种
        amountMap.put("currency", "CNY");
        params.put("amount", amountMap);
        String paramsStr = JSON.toJSONString(params);
        // todo 插入一条退款记录到数据库
        System.out.println("请求参数 ===> {}" + paramsStr);
        String res = wechatPayRequest.wechatHttpPost(url, paramsStr);
        System.out.println("退款结果：{"+res+"}");
    }
}
