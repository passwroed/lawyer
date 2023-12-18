package com.ruoyi.web.controller.lawyer.wx;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * @ClassName : WxMsgCallBackController
 * @Description : 微信消息回调
 * @Author : WANGKE
 * @Date: 2023-12-03 13:30
 */
@RestController
@RequestMapping("/wxmsg/callback")
public class WxMsgCallBackController extends BaseController {
    // 服务器配置填写的token
    private static final String wxToken = "eWSfUiujAmiqtbIzBly18ep4G5ohbiYo";
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private RestTemplate restTemplate;
    private WxMpService wxService = new WxMpServiceImpl();

    private WxMpMessageRouter messageRouter = new WxMpMessageRouter(this.wxService);

    @Anonymous
    @RequestMapping("/msg")
    public String get(HttpServletRequest request, String signature, String timestamp, String nonce, String echostr) {
        logger.info("微信消息开始：");
        if (request.getMethod().equalsIgnoreCase("GET")) {//用来校验，一般会验证前端配置的token等，这里简化了代码。
            logger.info("微信消息GET：" + echostr);
            return echostr;
        } else if (request.getMethod().equalsIgnoreCase("POST")) {//接收用户的相关行为事件结果
            logger.info("微信消息POST：");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder requestContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestContent.append(line);
                }
                reader.close();
                //接收：{"ToUserName":"gh_ea84a199bf81","FromUserName":"oG0NJ5Oi_3Dd1HlZJ14xfnA0sJ6s","CreateTime":1686131943,"MsgType":"event","Event":"subscribe_msg_popup_event","List":{"PopupScene":"0","SubscribeStatusString":"accept","TemplateId":"4ondVRxk4L20ihrJ3iI15BDK72XatGPxE0MeCVwHasQ"}}
                logger.info("接收：" + requestContent.toString());
                return "";
            } catch (IOException e) {
                // 处理异常情况
                e.printStackTrace();
                logger.error("异常：" + e.getMessage());
                return e.toString();
            }
        } else {
            logger.info("不是get 或 post方法");
            return null;
        }
    }

    @Anonymous
    @RequestMapping("/gzhMsg")
    public String gzh_msg(HttpServletRequest request, HttpServletResponse response, String signature, String timestamp, String nonce, String echostr) {
        if (request.getMethod().equalsIgnoreCase("GET")) {//用来校验，一般会验证前端配置的token等，这里简化了代码。
            try (OutputStream os = response.getOutputStream()) {
                String sha1 = getSHA1(wxToken, timestamp, nonce, "");

                // 和signature进行对比
                if (sha1.equals(signature)) {
                    // 返回echostr给微信
//                    os.write(URLEncoder.encode(echostr, "UTF-8").getBytes());
//                    os.flush();
                    return echostr;
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (request.getMethod().equalsIgnoreCase("POST")) {//接收用户的相关行为事件结果
            try {
                //解析
                //获取消息流,并解析xml
                WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());
                //消息类型
                String messageType = message.getMsgType();

                //消息事件
                String messageEvent = message.getEvent();
                //发送者帐号
                String openId = message.getFromUser();
                //开发者微信号
                String touser = message.getToUser();
                //文本消息  文本内容
                String text = message.getContent();
                //二维码参数
                String eventKey = message.getEventKey();
                String unionId = message.getUnionId();
                System.out.println(messageType);
                System.out.println(text);
                System.out.println(JSON.toJSONString(message));

                if (messageType.equals("event")) {
                    //获取微信用户信息
//            JSONObject userInfo = this.getUserInfo(openId);
                    System.out.println("openId=========：" + openId);
                    //根据不同的回调事件处理各自的业务
                    switch (messageEvent) {
                        //扫码
                        case "SCAN":
                            System.out.println("扫码");
                            //业务处理...
                            break;
                        //关注公众号
                        case "subscribe":
                            System.out.println("关注公众号");
                            //业务处理...
                            //获取unionid
                            String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", "wx4ed95367f5b89bac", "294e21e56c6465a650ab521c92d36b40");
                            String tokenRes = restTemplate.getForObject(tokenUrl, String.class);
                            JSONObject tokenObject = JSONObject.parseObject(tokenRes);

                            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + tokenObject.getString("access_token") + "&openid=" + openId + "&lang=zh_CN";
                            String res = restTemplate.getForObject(url, String.class);
                            JSONObject jsonObject = JSONObject.parseObject(res);
                            System.out.println("开始openid");
                            System.out.println(jsonObject.toString());
                            System.out.println(jsonObject.getString("unionid"));
                            if (StringUtils.isNotEmpty(jsonObject.getString("unionid"))) {
                                //查询有无用户有unionid
                                SysUser sysUser = new SysUser();
                                sysUser.setUnionId(jsonObject.getString("unionid"));
                                SysUser sysUser1 = iSysUserService.selectunionid(sysUser);
                                //如果有插入gzh_open_id
                                if (StringUtils.isNotNull(sysUser1)) {
                                    sysUser1.setGzhOpenId(openId);
                                    iSysUserService.updateUser(sysUser1);
                                }
                                System.out.println(openId);
                                //跟新用户数据
                            }
                            break;
                        //取消关注公众号
                        case "unsubscribe":
                            System.out.println("取消关注公众号");
                            //业务处理...
                            break;
                        default:
                            break;
                    }
                } else if (messageType.equals("text")) {

                    if (text.equals(""))
                    System.out.println("测试开始");
                    String out = null;
                    SortedMap<String, Object> map = new TreeMap<>();
//                    map.put("ToUserName", "<![CDATA["+"o2hvr6VBHvb9x8SYt7FsSNNpq2do"+"]]>");
//                    map.put("FromUserName", "<![CDATA["+"WL-20200323"+"]]>");
//                    map.put("CreateTime", System.currentTimeMillis());
//                    map.put("MsgType", "<![CDATA["+"text"+"]]>");
//                    map.put("Content", "<![CDATA["+"测试内容"+"]]>");
                    map.put("ToUserName", "<![CDATA["+openId+"]]>");
                    map.put("FromUserName", "<![CDATA["+touser+"]]>");
                    map.put("CreateTime", System.currentTimeMillis());
                    map.put("MsgType", "<![CDATA["+"text"+"]]>");
                    map.put("Content", "<![CDATA["+"测试内容"+"]]>");
                    out = mapToXml(map);
                    System.out.println(out);
                    try (OutputStream os = response.getOutputStream()) {
                        os.write(out.getBytes());
                        os.flush();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    return out;
                }
            } catch (Exception e) {

            }
        }


        return "";

    }

    public static String mapToXml(SortedMap<String, Object> sortedMap) {
        StringBuffer sb = new StringBuffer("<xml>");
        Iterator iterator = sortedMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = (String) iterator.next();
            Object value = sortedMap.get(key);
            sb.append("<" + key + ">");
            sb.append(value);
            sb.append("</" + key + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     * @throws Exception
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws Exception {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
