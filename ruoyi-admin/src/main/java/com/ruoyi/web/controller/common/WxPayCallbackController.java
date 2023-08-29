package com.ruoyi.web.controller.common;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.wxPat.WechatPayConfig;
import com.ruoyi.common.wxPat.WechatPayValidator;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.service.laywer.CostLogService;
import com.ruoyi.system.service.laywer.OrderService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName : WxPayCallbackController
 * @Description : 微信支付回调
 * @Author : WANGKE
 * @Date: 2023-08-26 02:47
 */
@RestController
@RequestMapping("/callback")
public class WxPayCallbackController {
    @Resource
    private WechatPayConfig wechatPayConfig;
    @Resource
    private OrderService orderService;

    @Resource
    private CostLogService costLogService;

    @Resource
    private Verifier verifier;

    private final ReentrantLock lock = new ReentrantLock();


    /**
     * 支付回调处理
     *
     * @param request
     * @param response
     * @return
     */
    @Anonymous
    @PostMapping("/payNotify")
    public Map<String, String> payNotify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("支付回调");

        // 处理通知参数
        Map<String, Object> bodyMap = getNotifyBody(request);
        if (bodyMap == null) {
            return falseMsg(response);
        }

        System.out.println("=========== 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱 ===========");
        if (lock.tryLock()) {
            try {
                // 解密resource中的通知数据
                String resource = bodyMap.get("resource").toString();
                Map<String, Object> resourceMap = WechatPayValidator.decryptFromResource(resource, wechatPayConfig.getApiV3Key(), 1);
                String orderNo = resourceMap.get("out_trade_no").toString();
                // String transactionId = resourceMap.get("transaction_id").toString();
                // 更改状态 获取订单号  修改订单状态为已支付
                // TODO 根据订单号，做幂等处理，并且在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
                System.out.println("=========== 根据订单号，做幂等处理 ===========");
                Order order = orderService.itemNo(orderNo);
                order.setStatus(1);
                if (orderService.edit(order) == 0){
                    System.out.println("订单"+orderNo+" 状态变更识别");
                }
                if (StringUtils.isNotNull(order.getTask())&&order.getType()==1){
                    CostLog costLog = new CostLog();
                    costLog.setType(2);
                    costLog.setCost(order.getMoney());
                    costLog.setLawyerId(order.getClientId());
                    if (costLogService.add(costLog) == 0){
                        System.out.println("订单"+orderNo+" 积分修改失败");
                    }
                }

            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }

        //成功应答
        return trueMsg(response);
    }

    /**
     * 退款回调处理
     *
     * @param request
     * @param response
     * @return
     */
    @Anonymous
    @PostMapping("/refundNotify")
    public Map<String, String> refundNotify(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("退款回调");

        // 处理通知参数
        Map<String, Object> bodyMap = getNotifyBody(request);
        if (bodyMap == null) {
            return falseMsg(response);
        }

        System.out.println("=========== 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱 ===========");
        if (lock.tryLock()) {
            try {
                // 解密resource中的通知数据
                String resource = bodyMap.get("resource").toString();
                Map<String, Object> resourceMap = WechatPayValidator.decryptFromResource(resource, wechatPayConfig.getApiV3Key(), 2);
                String orderNo = resourceMap.get("out_trade_no").toString();
//                String transactionId = resourceMap.get("transaction_id").toString();
                System.out.println("退款所有参数" + resourceMap);

                // TODO 根据订单号，做幂等处理，并且在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
                // 更改订单状态为已退款
                System.out.println("=========== 根据订单号，做幂等处理 ===========");

            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }

        //成功应答
        return trueMsg(response);
    }

    private Map<String, Object> getNotifyBody(HttpServletRequest request) {
        //处理通知参数
        String body = WechatPayValidator.readData(request);
        System.out.println("退款回调参数：{"+ body+"}");

        // 转换为Map
        Map<String, Object> bodyMap = JSONObject.parseObject(body, new TypeReference<Map<String, Object>>() {
        });
        // 微信的通知ID（通知的唯一ID）
        String notifyId = bodyMap.get("id").toString();
        // 验证签名信息
        WechatPayValidator wechatPayValidator
                = new WechatPayValidator(verifier, notifyId, body);
        if (!wechatPayValidator.validate(request)) {

            System.out.println("通知验签失败");
            return null;
        }
        System.out.println("通知验签成功");
        return bodyMap;
    }

    private Map<String, String> falseMsg(HttpServletResponse response) {
        Map<String, String> resMap = new HashMap<>(8);
        //失败应答
        response.setStatus(200);
        resMap.put("code", "ERROR");
        resMap.put("message", "通知验签失败");
        return resMap;
    }

    private Map<String, String> trueMsg(HttpServletResponse response) {
        Map<String, String> resMap = new HashMap<>(8);
        //成功应答
        response.setStatus(200);
        resMap.put("code", "SUCCESS");
        resMap.put("message", "成功");
        return resMap;
    }
}
