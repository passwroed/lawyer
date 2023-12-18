package com.ruoyi.web.controller.common;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest;
import com.aliyun.dysmsapi20170525.models.AddSmsTemplateResponse;
import com.aliyun.tea.TeaException;
import com.ruoyi.common.config.WxLawyerAppConfig;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : WxMsgSend
 * @Description :
 * @Author : WANGKE
 * @Date: 2023-12-03 21:32
 */
@Component
//@ComponentScan
public class WxMsgSend {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private WxLawyerAppConfig wxLawyerAppConfig;
    private static String template_id = "xIBDB9_72W8foiNva2dOBJYP-_rWJiCUXrpjmcWvq-w";

    public void sendMsg( List<String> openIds, Task task,Long id){
        System.out.println("发送消息--------------开始");
        String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", "wx4ed95367f5b89bac", "294e21e56c6465a650ab521c92d36b40");
        String tokenRes = restTemplate.getForObject(tokenUrl, String.class);
        System.out.println(tokenRes);
        JSONObject tokenObject = JSONObject.parseObject(tokenRes);
        SysDictData dictData1 = new SysDictData();
        dictData1.setDictType("casesource_type");
        dictData1.setDictValue("1");
        List<SysDictData> list = dictDataService.selectDictDataList(dictData1);
        String typeName = "";
        for (SysDictData dictData:list) {
            if (dictData.getDictValue().equals(String.valueOf(task.getType()))){
                typeName = dictData.getDictLabel();
            }
        }
        for (String openid:openIds) {
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + tokenObject.getString("access_token");
            Map<String, Object> data = new HashMap<>();
            DateFormat customDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(typeName);
            System.out.println(task.getNo());
            System.out.println(String.valueOf(task.getProfit())+"万元");
            System.out.println(task.getPovince());
            data.put("time7",new WeChatTemplateMsg(customDateFormat.format(new Date())));
//            data.put("const10",new WeChatTemplateMsg(typeName));
            data.put("number2",new WeChatTemplateMsg(task.getNo()));
            data.put("amount11",new WeChatTemplateMsg(String.valueOf(task.getProfit()*10000)+"元"));
            data.put("thing12",new WeChatTemplateMsg(task.getPovince()));
            //封装请求体
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("template_id", template_id);
            paramMap.put("touser",openid);
            paramMap.put("data",data);
            Map<String,Object> miniprogram = new HashMap<>();
            miniprogram.put("appid",wxLawyerAppConfig.getAppId());
            if (id >0l){
                miniprogram.put("pagepath","/pages/index/mytask/localtask/localtask");
            }else {
                miniprogram.put("pagepath","/pages/index/taskhall/halldetails/halldetails?id="+task.getId());
            }


            paramMap.put("miniprogram",miniprogram);


//            "{ \"appid\":\""+wxLawyerAppConfig.getAppId()+"\", \"pagepath\":  \"pages/index/taskhall/taskhall\"  }"
            //封装请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap,headers);
            JSONObject phoneObject = JSONObject.parseObject(restTemplate.postForEntity(url, httpEntity,String.class).getBody());
            System.out.println(phoneObject.toString());
        }
        System.out.println("发送消息--------------结束");

    }




    public class WeChatTemplateMsg implements Serializable {
        /**
         * 消息
         */
        private String value;

        public WeChatTemplateMsg(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }


    public void sendSMS(String phone,String orderNum,String content) throws Exception {
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.dysmsapi20170525.Client client = this.createClient("LTAI5tBwjwn7GdkCrR1v3Wz7", "82YvAQqKdQcsyBXkrsLVuSYQGIhWTZ");
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phone)
                .setTemplateCode("SMS_464076193")
                .setSignName("贵州青律之家法律研究院")
                .setTemplateParam("{\"orderNum\":\""+orderNum+"\",\"content\":\""+content+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
