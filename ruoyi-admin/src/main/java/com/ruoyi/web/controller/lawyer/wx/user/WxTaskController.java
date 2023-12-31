package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.TaskLogService;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : WxTaskController
 * @Description : 任务大厅
 * @Author : WANGKE
 * @Date: 2023-08-30 20:51
 */
@RestController
@RequestMapping("/wxuser/task")
public class WxTaskController extends BaseController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskLogService taskLogService;


    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:task:list')")
    @Anonymous
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Task task) {
        startPage();
        task.setFastLawyerId(2l);
        return taskService.listToPassword(task);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Task task) {
        task = taskService.item(task.getId());
        if (StringUtils.isNotNull(task.getPhone())){
            task.setPhone(task.getPhone().replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2"));
        }
        return success(task);
    }
}
