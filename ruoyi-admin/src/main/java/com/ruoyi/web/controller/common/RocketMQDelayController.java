package com.ruoyi.web.controller.common;

import com.alibaba.fastjson2.JSONObject;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : RocketMQDelayController
 * @Description : 发送延迟消息
 * @Author : WANGKE
 * @Date: 2023-09-07 09:38
 */
@RestController
public class RocketMQDelayController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送延时消息
     */
    @RequestMapping("/testDelaySend")
    public void testDelaySend(){

        Map<String,Object> orderMap = new HashMap<>();
        orderMap.put("id","1357890");
        orderMap.put("createTime", LocalDateTime.now());

        //参数一：topic   如果想添加tag,可以使用"topic:tag"的写法
        //参数二：Message<?>
        //参数三：消息发送超时时间
        //参数四：delayLevel 延时level  messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        rocketMQTemplate.syncSend("test-topic-delay", MessageBuilder.withPayload(JSONObject.toJSONString(orderMap)).build(),3000,1);
        System.out.println("发送成功");
    }
}
