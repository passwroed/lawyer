package com.ruoyi.web.controller.common;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.wxPat.WechatPayConfig;
import com.ruoyi.common.wxPat.WechatPayValidator;
import com.ruoyi.system.domain.lawyer.*;
import com.ruoyi.system.service.laywer.*;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private GoodsService goodsService;
    @Resource
    private TaskService taskService;

    @Resource
    private LawyerService lawyerService;
    @Autowired
    private MsgService msgService;
    @Autowired
    private  WxMsgSend wxMsgSend;
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
                System.out.println("--------- 订单NO："+orderNo+"-----");
                // String transactionId = resourceMap.get("transaction_id").toString();
                // 更改状态 获取订单号  修改订单状态为已支付
                // TODO 根据订单号，做幂等处理，并且在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
                System.out.println("=========== 根据订单号，做幂等处理 ===========");
                Order order = orderService.itemNo(orderNo);
                System.out.println("订单 --- "+JSONObject.toJSONString(order));
                System.out.println(order.getId());
                order.setStatus(2);
                if (order.getType()==1){
                    CostLog costLog = new CostLog();
                    costLog.setType(2);
                    costLog.setCost(order.getMoney());
                    costLog.setLawyerId(order.getClientId());
                    if (costLogService.add(costLog) == 0){
                        System.out.println("订单"+orderNo+" 积分修改失败");
                    }
                }else {
                    //添加任务
                    Task task = taskService.itemOrderNo(order.getNo());
                    System.out.println("任务更新"+JSONObject.toJSONString(task));
                    if (StringUtils.isNotNull(task)){
                        Task task1 = new Task();
                        task1.setId(task.getId());
                        if (taskService.editStatus0(task1) == 0){
                            System.out.println("任务更新失败");
                        }
                        Lawyer lawyer = new Lawyer();
                        lawyer.setType(0);
                        List<Lawyer> list = lawyerService.typeListOpenId(lawyer);
                        List<String> openIds = new ArrayList<>();
                        for (Lawyer lawyer1:list) {
                            if (StringUtils.isNotEmpty(lawyer1.getGzhOpenId())){
                                openIds.add(lawyer1.getGzhOpenId());
                            }
                        }
                        if (openIds.size()>0)
                        wxMsgSend.sendMsg(openIds,task,0l);
                    }
                    Msg msg = new Msg();
                    msg.setClientId(order.getClientId());
                    msg.setType(1);
                    msg.setStatus(0);
                    msg.setMsg("尊敬的用户您好：您的编号为："+orderNo+"的订单，"+"已购买成功，"+"您可以前往我的->我的订单中查看详情进度");

                    msgService.add(msg);

                }
                order.setPayTime(new Date());

                if (orderService.edit(order) == 0){
                    System.out.println("订单"+orderNo+" 状态变更识别");
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
