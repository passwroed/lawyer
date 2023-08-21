package com.ruoyi.web.controller.lawyer.wx.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxTaskController
 * @Description : 微信任务大厅
 * @Author : WANGKE
 * @Date: 2023-08-16 18:20
 */
@RestController
@RequestMapping("/wxLawyer/task")
public class WxTaskController extends BaseController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Task task)
    {
        startPage();
        List<Task> list = taskService.list(task);
        return getDataTable(list);
    }
}
