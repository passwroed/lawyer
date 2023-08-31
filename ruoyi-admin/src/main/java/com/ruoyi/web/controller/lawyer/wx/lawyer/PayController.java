package com.ruoyi.web.controller.lawyer.wx.lawyer;

import com.ruoyi.common.config.WxLawyerAppConfig;
import com.ruoyi.common.config.WxUserAppConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.service.laywer.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * @ClassName : PayController
 * @Description : 充值
 * @Author : WANGKE
 * @Date: 2023-08-27 00:20
 */
@RestController
@RequestMapping("/wxLawyer/pay")
public class PayController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Resource
    private WxLawyerAppConfig wxLawyerAppConfig;
    @PostMapping("/recharge")
    public AjaxResult recharge(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getMoney())){
            return error("请输入充值金额");
        }
        //充值订单order创建
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        Random r=new Random();
        order.setNo(sdf.format(System.currentTimeMillis())+r.nextInt(10));//规则：时间+1位随机数
        order.setGoodsName("充值订单");
        order.setStatus(0);
        order.setType(1);
        order.setClientId(getLawyerId());
        order.setClientName(getLawyerName());
        return success(orderService.add(order, wxLawyerAppConfig.getAppId()));
    }
}
