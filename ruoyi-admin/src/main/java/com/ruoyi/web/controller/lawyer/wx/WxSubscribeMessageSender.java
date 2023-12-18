package com.ruoyi.web.controller.lawyer.wx;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.WxLawyerAppConfig;
import com.ruoyi.common.config.WxUserAppConfig;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import io.netty.handler.codec.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : WeChatSubscribeMessageSender
 * @Description : 微信消息
 * @Author : WANGKE
 * @Date: 2023-12-03 14:56
 */
public class WxSubscribeMessageSender {

}
