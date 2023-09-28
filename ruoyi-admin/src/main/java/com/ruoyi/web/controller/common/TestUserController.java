package com.ruoyi.web.controller.common;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : TestUserController
 * @Description :
 * @Author : WANGKE
 * @Date: 2023-09-08 09:34
 */
@RestController
@RequestMapping("/test")
public class TestUserController extends BaseController {
    @Resource
    private TaskService taskService;
    @RequestMapping("/testuser")
    public void testuser(){

        Task task1 = new Task();
        task1.setId(26l);
        task1.setStatus(0);
        taskService.edit(task1);
    }
}
