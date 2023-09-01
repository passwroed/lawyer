package com.ruoyi.web.controller.lawyer.wx.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.domain.lawyer.TaskLog;
import com.ruoyi.system.service.laywer.CostLogService;
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
 * @Description : 微信任务大厅
 * @Author : WANGKE
 * @Date: 2023-08-16 18:20
 */
@RestController
@RequestMapping("/wxLawyer/task")
public class WLTaskController extends BaseController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private LawyerService lawyerService;
    @Autowired
    private CostLogService costLogService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Task task) {
        System.out.println(getLawyerId());
        System.out.println(getLawyerName());
        System.out.println(getLawyerType());
        if (StringUtils.isNull(getLawyerId())) {
            return errorDataTable("无权限，请使用律师账号登录");
        }
        startPage();
        List<Task> list;
        if (getLawyerType() == 0) {
            //中台律师
            task.setFastLawyerId(null);
            list = taskService.list(task);
        } else if (getLawyerType() == 1) {
            //当地律师
            task.setLawyerId(getLawyerId());
            list = taskService.lawyer1list(task);
        } else {
            return errorDataTable("用户信息获取失败，请重新登陆");
        }
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

    @PostMapping("/item")
    public AjaxResult item(@RequestBody Task task) {
        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("参数错误");
        }
        task = taskService.item(task.getId());
        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("查询错误");
        }
        if (task.getLawyerId() != getLawyerId() && task.getFastLawyerId() != getLawyerId()){
            task.setPhone(task.getPhone().replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2"));
        }
        return success(task);
    }

    @PostMapping("/get")
    public AjaxResult get(@RequestBody Task task) {
        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("参数错误");
        }
        Task taskGet = new Task();
        taskGet.setId(task.getId());
        taskGet.setUpdateBy(getLawyerName());
        //查询当前律师类型
        if (getLawyerType() == 0) {
            //如果是中台律师，可以直接接
            Task task1 = taskService.item(task.getId());
            if (StringUtils.isNull(task1) || StringUtils.isNotNull(task1.getFastLawyerId())) {
                return error("任务id错误或者已被领取");
            }

            Lawyer lawyer = lawyerService.item(getLawyerId());
            if (StringUtils.isNull(lawyer) || StringUtils.isNull(lawyer.getType()) || lawyer.getType() == 1) {
                return error("你尚未是中台律师，无法领取任务，请联系管理员！");
            }
            TaskLog taskLog = new TaskLog();
            taskLog.setTaskId(task.getId());
            taskLog.setStatus(1);
            taskLog.setFastLawyerId(lawyer.getId());
            taskLog.setFastLawyerName(lawyer.getName());
            taskLog.setFastLawyerType(lawyer.getType());
            taskLog.setContent(task.getContent());
            taskLog.setWilling(task.getWilling());

            if (taskLogService.add(taskLog) == 0) {
                return error("领取失败，请联系管理员！");
            }

            taskGet.setStatus(1);
            taskGet.setFastLawyerId(lawyer.getId());
            taskGet.setFastLawyerName(lawyer.getName());
            taskGet.setFastLawyerType(lawyer.getType());
            if (taskService.edit(taskGet) == 0) {
                return error("领取失败，请联系管理员！");
            }
            return success("操作成功");
        } else if (getLawyerType() == 1) {
            //如果是当地律师，查询积分是否够
            CostLog costLog = costLogService.newCostLog(getLawyerId());
            if (costLog.getSum() < task.getCost()) {
                //积分不够
                return error("您的积分不够，请充值");
            }
            costLog = new CostLog();
            costLog.setType(3);
            costLog.setCost(task.getCost());
            costLog.setLawyerId(getLawyerId());
            costLog.setTaskId(Long.valueOf(taskGet.getNo()));
            if (costLogService.add(costLog) == 0) {
                return error("系统错误");
            }
            Lawyer lawyer = lawyerService.item(getLawyerId());
            if (StringUtils.isNull(lawyer)) {
                return error("无法领取任务，请联系管理员！");
            }
            task.setLawyerType(lawyer.getType());
            task.setLawyerName(lawyer.getName());
            task.setLawyerId(getLawyerId());
            task.setStatus(6);
            task.setPayStatus(2);

            TaskLog taskLog = new TaskLog();
            taskLog.setTaskId(task.getId());
            taskLog.setStatus(1);
            taskLog.setFastLawyerId(lawyer.getId());
            taskLog.setFastLawyerName(lawyer.getName());
            taskLog.setFastLawyerType(lawyer.getType());
            taskLog.setContent(task.getContent());
            taskLog.setWilling(task.getWilling());

            if (taskLogService.add(taskLog) == 0) {
                return error("领取失败，请联系管理员！");
            }

            taskGet.setStatus(1);
            taskGet.setFastLawyerId(lawyer.getId());
            taskGet.setFastLawyerName(lawyer.getName());
            taskGet.setFastLawyerType(lawyer.getType());
            if (taskService.edit(taskGet) == 0) {
                return error("领取失败，请联系管理员！");
            }
            return success("操作成功");
        } else {
            return error("用户信息获取失败，请重新登陆");
        }
    }

    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody Task task) {
        //状态改回3

        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("参数错误");
        }
        task = taskService.item(task.getId());
        if (task.getStatus() != 4 || task.getLawyerId() != getLawyerId()) {
            return error("无权拒绝该任务");
        }
        if (taskService.reject(task.getId()) == 0) {
            return error("操作失败");
        }

        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setStatus(-1);
        taskLog.setFastLawyerId(task.getFastLawyerId());
        taskLog.setFastLawyerName(task.getFastLawyerName());
        taskLog.setFastLawyerType(task.getFastLawyerType());
        taskLog.setLawyerId(task.getLawyerId());
        taskLog.setLawyerName(task.getLawyerName());
        taskLog.setLawyerType(task.getLawyerType());
        taskLog.setRemark("拒绝");
        if (taskLogService.add(taskLog) == 0) {
            return error("领取失败，请联系管理员！");
        }
        //写入日志
        return success();
    }

    @PostMapping("/myList")
    public TableDataInfo myList(@RequestBody Task task) {
        Task task1 = new Task();
        if (getLawyerType() == 0){
            task1.setFastLawyerId(getLawyerId());
        }else {
            task1.setFastLawyerId(1l);
            task1.setLawyerId(getLawyerId());
        }
        if (StringUtils.isNotNull(task.getStatus())){
            task1.setStatus(task.getStatus());
        }
        startPage();
        List<Task> list = taskService.list(task1);
        return getDataTable(list);
    }

    @PostMapping("/taskLog")
    public TableDataInfo taskLog(@RequestBody Task task) {
        if (StringUtils.isNull(task.getId())) {
            return errorDataTable();
        }
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        startPage();
        List<TaskLog> list = taskLogService.list(taskLog);
        return getDataTable(list);
    }

    @PostMapping("/followUp")
    public AjaxResult followUp(@RequestBody Task task) {
        if (StringUtils.isNull(task)
                || StringUtils.isNull(task.getId())
                || StringUtils.isNull(task.getStatus())
                || StringUtils.isNull(task.getContent())) {
            return error("参数错误！");
        }
        Task task2 = taskService.item(task.getId());
        task.setFastLawyerId(task2.getFastLawyerId());
        task.setFastLawyerName(task2.getFastLawyerName());
        task.setFastLawyerType(task2.getFastLawyerType());

        TaskLog taskLog = new TaskLog();
        switch (task.getStatus()) {
            case 1:
                taskLog.setRemark("待交付");
                break;
            case 2:
                taskLog.setRemark("完成交付");
                break;
            case 3:
                //平台跟进
                taskLog.setRemark("平台跟进");
                break;
            case 4:
                //指定律师
                if (StringUtils.isNull(task.getCost()) || task.getCost() <= 0) {
                    return error("需要指定所需积分");
                }
                if (StringUtils.isNull(task.getLawyerId())) {
                    return error("需要指定律师");
                }
                Lawyer lawyer2 = lawyerService.item(task.getLawyerId());
                task.setPayStatus(1);
                task.setLawyerId(lawyer2.getId());
                task.setLawyerName(lawyer2.getName());
                task.setLawyerType(lawyer2.getType());
                taskLog.setLawyerId(lawyer2.getId());
                taskLog.setLawyerName(lawyer2.getName());
                taskLog.setLawyerType(lawyer2.getType());
                taskLog.setRemark("指派当地律师");
                break;
            case 5://转入大厅
                if (StringUtils.isNull(task.getCost()) || task.getCost() <= 0) {
                    return error("需要指定所需积分");
                }
                task.setIsHall(1);
                taskLog.setRemark("转入案源大厅");
                break;
            case 6:
                if (StringUtils.isNull(task.getProfit())) {
                    return error("请输入签约金额");
                }
                taskLog.setRemark("平台委托成功");
                break;
            case 7://持续跟进
                Task task1 = taskService.item(task.getId());
                Lawyer lawyer = lawyerService.item(task1.getLawyerId());
                if (StringUtils.isNull(lawyer)) {
                    return error("未找到此任务");
                }
                if (StringUtils.isNull(lawyer.getUserId())) {
                    return error("当前定任务，未绑定律师");
                }
                if (!lawyer.getUserId().equals(getUserId())) {
                    return error("此任务已被其他律所领取，您无法修改");
                }
                taskLog.setRemark("持续跟进");
                break;
            case 8:
                if (StringUtils.isNull(task.getProfit())) {
                    return error("请输入签约金额");
                }
                taskLog.setRemark("已成功委托");
                break;
            case 9:
                taskLog.setRemark("结束任务");
                break;
        }

        taskLog.setTaskId(task.getId());
        taskLog.setStatus(task.getStatus());
        taskLog.setFastLawyerId(task.getFastLawyerId());
        taskLog.setFastLawyerName(task.getFastLawyerName());
        taskLog.setFastLawyerType(task.getFastLawyerType());
        taskLog.setContent(task.getContent());
        taskLog.setWilling(task.getWilling());
        taskLog.setProfit(task.getProfit());

        if (taskLogService.add(taskLog) == 0) {
            return error("编辑失败，请联系管理员！");
        }
        //转入大厅
        task.setUpdateBy(getUsername());
        if (taskService.edit(task) == 0) {
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }

}
