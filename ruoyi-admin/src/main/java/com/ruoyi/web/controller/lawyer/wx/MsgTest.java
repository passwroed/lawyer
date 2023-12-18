package com.ruoyi.web.controller.lawyer.wx;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.WxLawyerAppConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.TaskService;
import com.ruoyi.web.controller.common.WxMsgSend;
import com.sun.org.apache.xml.internal.serialize.Serializer;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : MsgTest
 * @Description : 测试
 * @Author : WANGKE
 * @Date: 2023-12-03 20:09
 */
@RestController
@RequestMapping("/wxmsg/callback/test")
public class MsgTest extends BaseController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private LawyerService lawyerService;
    @Autowired
    private  WxMsgSend wxMsgSend;
    @Autowired
    private RestTemplate restTemplate;
    @Anonymous
    @RequestMapping("/test")
    public void get() {
        Lawyer lawyer = new Lawyer();
        lawyer.setType(0);
        List<Lawyer> list = lawyerService.typeListOpenId(lawyer);
        List<String> openIds = new ArrayList<>();
        for (Lawyer lawyer1:list) {
            if (StringUtils.isNotEmpty(lawyer1.getOpenId())){
                openIds.add(lawyer1.getOpenId());
            }
        }
        openIds.add("o2hvr6S70KhPohno004VRJvRpqfY");
        wxMsgSend.sendMsg(openIds,taskService.item(26l),0l);
    }
    @Anonymous
    @RequestMapping("/sendSMS")
    public void sendSMS() throws Exception {
//        wxMsgSend.sendSMS("---订单号---","---内容---");
    }
    @Anonymous
    @RequestMapping("/generatescheme")
    public void generatescheme() throws Exception {
        String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", "wxca18f4697687b04b", "91e91a1092f0a9ed09424136996d2f8d");
        String tokenRes = restTemplate.getForObject(tokenUrl, String.class);
        System.out.println(tokenRes);
        JSONObject tokenObject = JSONObject.parseObject(tokenRes);
        String generateschemeUrl = String.format("https://api.weixin.qq.com/wxa/generatescheme?access_token="+tokenObject.getString("access_token"));
        String generatescheme = restTemplate.postForObject(generateschemeUrl,null, String.class);
        System.out.println(generatescheme);
        JSONObject generateschemeObject = JSONObject.parseObject(generatescheme);
        String openlink = generateschemeObject.getString("openlink");
    }

}
