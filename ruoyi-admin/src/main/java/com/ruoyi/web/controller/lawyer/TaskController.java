package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.domain.lawyer.TaskLog;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.OrderService;
import com.ruoyi.system.service.laywer.TaskLogService;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName : TaskController
 * @Description : 任务
 * @Author : WANGKE
 * @Date: 2023-08-02 16:07
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private LawyerService lawyerService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private OrderService orderService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:task:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Task task) {
        startPage();
        task.setFastLawyerId(null);
        task.setStatus(null);
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        for (SysRole role : listRole) {
            if (role.getRoleKey().equals("admin") || role.getRoleKey().equals("general")) {
                task.setFastLawyerId(2l);
                task.setStatus(null);
            }
            if (role.getRoleKey().equals("kefu")) {
                task.setFastLawyerId(2l);
                task.setStatus(null);
                task.setPid(getUserId());
            }
        }
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

    @PostMapping("/myList")
    public TableDataInfo myList(@RequestBody Task task) {
        startPage();
        task.setFastLawyerId(null);
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        for (SysRole role : listRole) {
            if (role.getRoleKey().equals("admin") || role.getRoleKey().equals("general")) {
                task.setFastLawyerId(1l);
            }
        }

        if (StringUtils.isNull(task.getFastLawyerId()) || task.getFastLawyerId() != 1l) {
            Lawyer lawyer = new Lawyer();
            lawyer.setUserId(getUserId());
            List<Lawyer> listLawyer = lawyerService.selectUserId(lawyer);
            if (listLawyer.size() == 0) {
                return errorDataTable();
            }
            task.setFastLawyerId(listLawyer.get(0).getId());
        }
        List<Task> list = taskService.list(task);
        return getDataTable(list);
    }

    @PostMapping("/item")
    public AjaxResult item(@RequestBody Task task) {
        return success(taskService.item(task.getId()));
    }

    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:task:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Task task) {

        task.setCreateBy(getUsername());
        if (taskService.add(task) == 0) {
            return error("新增失败，请联系管理员！");
        }
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setStatus(0);
        taskLog.setRemark("手动创建任务");
        taskLogService.add(taskLog);

        return success("操作成功");
    }

    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:task:edit')")
    @PostMapping("/get")
    public AjaxResult get(@RequestBody Task task) {
        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("参数错误！");
        }
        Task task1 = taskService.item(task.getId());
        if (StringUtils.isNull(task1) || task1.getStatus() > 0) {
            return error("任务id错误或者已被领取");
        }
        Task task2 = taskService.item(task.getId());
        if (StringUtils.isNotNull(task2.getOrderNo())){
            Order order = new Order();
            order.setNo(task2.getOrderNo());
            order = orderService.itemNo(order.getNo());
            if (StringUtils.isNotNull(order)){
                order.setStatus(2);
                orderService.edit(order);
            }
        }


        Task taskGet = new Task();
        taskGet.setId(task.getId());
        taskGet.setUpdateBy(getUsername());
        Lawyer lawyer = new Lawyer();
        lawyer.setUserId(getUserId());
        List<Lawyer> lawyerList = lawyerService.selectUserId(lawyer);
        if (lawyerList.size() == 0) {
            return error("你尚未是中台律师，无法领取任务，请联系管理员！");
        } else {
            lawyer = lawyerList.get(0);
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
    }

    @PostMapping("/assign")
    public AjaxResult assign(@RequestBody Task task) {
        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("参数错误！");
        }
        Task taskGet = new Task();
        taskGet.setId(task.getId());
        task.setUpdateBy(getUsername());
        Lawyer lawyer = lawyerService.item(task.getLawyerId());
        if (StringUtils.isNull(lawyer)&&StringUtils.isNull(lawyer.getType())&&lawyer.getType()!=0) {
            return error("尚未是中台律师，无法领取任务，请联系管理员！");
        }
        taskGet.setFastLawyerId(lawyer.getId());
        taskGet.setFastLawyerName(lawyer.getName());
        taskGet.setFastLawyerType(lawyer.getType());
        if (taskService.edit(taskGet) == 0) {
            return error("绑定失败，请联系管理员！");
        }
        return success("操作成功");
    }

//    @PostMapping("/changeAssign")
//    public AjaxResult changeAssign(@RequestBody Task task)
//    {
//        if (StringUtils.isNull(task)||StringUtils.isNull(task.getId())){
//            return error("参数错误！");
//        }
//        Task taskGet = new Task();
//        taskGet.setId(task.getId());
//        task.setUpdateBy(getUsername());
//        Lawyer lawyer = new Lawyer();
//        lawyer.setId(task.getLawyerId());
//        List<Lawyer> lawyerList = lawyerService.selectUserId(lawyer);
//        if (lawyerList.size()==0){
//            return error("尚未是中台律师，无法领取任务，请联系管理员！");
//        }else {
//            lawyer = lawyerList.get(0);
//        }
//        taskGet.setLawyerId(lawyer.getId());
//        taskGet.setLawyerName(lawyer.getName());
//        taskGet.setLawyerType(lawyer.getType());
//        if (taskService.edit(taskGet) == 0){
//            return error("绑定失败，请联系管理员！");
//        }
//        return success("操作成功");
//    }

    @PostMapping("/followUp")
    public AjaxResult followUp(@RequestBody Task task) {
        if (StringUtils.isNull(task)
                || StringUtils.isNull(task.getId())
                || StringUtils.isNull(task.getStatus())
                || StringUtils.isNull(task.getContent())) {
            return error("参数错误！");
        }
        Task task2 = taskService.item(task.getId());
        if (StringUtils.isNotNull(task2.getOrderNo())&&task.getStatus()>1){
            Order order = new Order();
            order.setNo(task2.getOrderNo());
            order = orderService.itemNo(order.getNo());
            if (StringUtils.isNotNull(order)){
                switch (task.getStatus()){
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        order.setStatus(2);
                        break;
                    case 8:
                    case 9:
                        order.setStatus(3);
                        break;
                }
                orderService.edit(order);
            }
        }

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
                Lawyer lawyer2 =lawyerService.item(task.getLawyerId());
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

    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Task task) {
        if (StringUtils.isNull(task) || StringUtils.isNull(task.getId())) {
            return error("参数错误！");
        }
        task.setUpdateBy(getUsername());
        if (taskService.edit(task) == 0) {
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }

    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:task:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Task task) {
        if (StringUtils.isNull(task.getId())) {
            return error("参数错误！");
        }
        if (taskService.del(task.getId()) == 0) {
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }

    @PostMapping("/taskLog")
    public TableDataInfo taskLog(@RequestBody Task task) {
        if (StringUtils.isNull(task.getId())) {
            return errorDataTable();
        }
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        List<TaskLog> list = taskLogService.list(taskLog);
        return getDataTable(list);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody Task task)
    {
        List<Task> list = taskService.list(task);
        ExcelUtil<Task> util = new ExcelUtil<Task>(Task.class);
        util.exportExcel(response, list, "任务数据数据");
    }
}
