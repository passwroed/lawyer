package com.ruoyi.web.controller.common;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.service.laywer.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName : RocketMQDelayConsumerListener
 * @Description : 接受消息
 * @Author : WANGKE
 * @Date: 2023-09-07 09:41
 */
@Service
@RocketMQMessageListener(consumerGroup = "test-delay",topic = "test-topic-delay")
public class RocketMQDelayConsumerListener implements RocketMQListener<String> {
    @Autowired
    private OrderService orderService;
    @Override
    public void onMessage(String s) {
        Map<String,Object> orderMap = JSONObject.parseObject(s,Map.class);
        if (StringUtils.isNotNull(orderMap.get("id"))){
            Long id = Long.valueOf(String.valueOf(orderMap.get("id")));
            System.out.println("consumer 延时消息消费"+s);
            Order order = orderService.item(id);
            if (StringUtils.isNotNull(order)){
                System.out.println("更新订单  id"+order.getId());
                Order order1 = new Order();
                order1.setId(order.getId());
                order1.setStatus(-1);
                orderService.edit(order1);
            }
        }

    }
}
