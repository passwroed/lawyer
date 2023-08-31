package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.core.controller.BaseController;
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


    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:task:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Task task) {
        startPage();
        task.setIsHall(1);
        task.setFastLawyerId(1L);
        List<Task> list = taskService.list(task);
        List<Task> returnlist = new ArrayList<>();
        if (list.size() > 0 && StringUtils.isNull(task.getFastLawyerId())) {
            for (Task task1 : list) {
                if (StringUtils.isNotNull(task1.getPhone())) {
                    task1.setPhone(task1.getPhone().replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2"));
                    returnlist.add(task1);
                }

            }
        } else {
            returnlist = list;
        }
        return getDataTable(returnlist);
    }
}
