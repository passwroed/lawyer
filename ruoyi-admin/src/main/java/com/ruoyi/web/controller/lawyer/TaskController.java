package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:task:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Task task)
    {
        startPage();
        List<Task> list = taskService.list(task);
        return getDataTable(list);
    }
    @PostMapping("/myList")
    public TableDataInfo myList(@RequestBody Task task)
    {
        startPage();
        Lawyer lawyer = new Lawyer();
        lawyer.setUserId(getUserId());
        List<Lawyer> listLawyer = lawyerService.selectUserId(lawyer);
        if (listLawyer.size() == 0 ){
            return errorDataTable();
        }
        task.setLawyerId(listLawyer.get(0).getId());
        List<Task> list = taskService.list(task);
        return getDataTable(list);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Task task)
    {
        return success(taskService.item(task.getId()));
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:task:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Task task)
    {
        task.setCreateBy(getUsername());
        if (taskService.add(task) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:task:edit')")
    @PostMapping("/get")
    public AjaxResult get(@RequestBody Task task)
    {
        if (StringUtils.isNull(task)||StringUtils.isNull(task.getId())){
            return error("参数错误！");
        }
        Task taskGet = new Task();
        taskGet.setId(task.getId());
        taskGet.setUpdateBy(getUsername());
        Lawyer lawyer = new Lawyer();
        lawyer.setUserId(getUserId());
        List<Lawyer> lawyerList = lawyerService.selectUserId(lawyer);
        if (lawyerList.size()==0){
            return error("你尚未是中台律师，无法领取任务，请联系管理员！");
        }else {
            lawyer = lawyerList.get(0);
        }
        taskGet.setLawyerId(lawyer.getId());
        taskGet.setLawyerName(lawyer.getName());
        taskGet.setLawyerType(lawyer.getType());
        if (taskService.edit(taskGet) == 0){
            return error("领取失败，请联系管理员！");
        }
        return success("操作成功");
    }

    @PostMapping("/assign")
    public AjaxResult assign(@RequestBody Task task)
    {
        if (StringUtils.isNull(task)||StringUtils.isNull(task.getId())){
            return error("参数错误！");
        }
        Task taskGet = new Task();
        taskGet.setId(task.getId());
        task.setUpdateBy(getUsername());
        Lawyer lawyer = new Lawyer();
        lawyer.setId(task.getLawyerId());
        lawyer.setType(0);
        List<Lawyer> lawyerList = lawyerService.selectUserId(lawyer);
        if (lawyerList.size()==0){
            return error("尚未是中台律师，无法领取任务，请联系管理员！");
        }else {
            lawyer = lawyerList.get(0);
        }
        taskGet.setLawyerId(lawyer.getId());
        taskGet.setLawyerName(lawyer.getName());
        taskGet.setLawyerType(lawyer.getType());
        if (taskService.edit(taskGet) == 0){
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
    public AjaxResult followUp(@RequestBody Task task)
    {
        if (StringUtils.isNull(task)
                ||StringUtils.isNull(task.getId())
                ||StringUtils.isNull(task.getStatus())
                ||StringUtils.isNull(task.getContent())
                ||StringUtils.isNull(task.getWilling())){
            return error("参数错误！");
        }
        switch (task.getStatus()){
            case 6://持续跟进
                Task task1 = taskService.item(task.getId());
                Lawyer lawyer = lawyerService.item(task1.getLawyerId());
                if (StringUtils.isNull(lawyer)){
                    return error("未找到此任务");
                }
                if (StringUtils.isNull(lawyer.getUserId())) {
                    return error("当前定任务，未绑定律师");
                }
                if (!lawyer.getUserId().equals(getUserId())) {
                    return error("此任务已被其他律所领取，您无法修改");
                }
                break;
            case 3:
                //指定律师
                if (StringUtils.isNull(task.getCost())||task.getCost()<=0){
                    return error("需要指定所需积分");
                }
                if (StringUtils.isNull(task.getLawyerId())){
                    return error("需要指定律师");
                }
                Lawyer lawyer1 = new Lawyer();
                lawyer1.setId(task.getLawyerId());
                List<Lawyer> lawyerList = lawyerService.selectUserId(lawyer1);
                if (lawyerList.size()==0){
                    return error("未找到指定律师！");
                }else {
                    lawyer1 = lawyerList.get(0);
                }
                task.setLawyerId(lawyer1.getId());
                task.setLawyerName(lawyer1.getName());
                task.setLawyerType(lawyer1.getType());
                break;
            case 4://转入大厅
                if (StringUtils.isNull(task.getCost())||task.getCost()<=0){
                    return error("需要指定所需积分");
                }
                break;
        }

        //转入大厅
        task.setUpdateBy(getUsername());
        if (taskService.edit(task) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Task task)
    {
        if (StringUtils.isNull(task)||StringUtils.isNull(task.getId())){
            return error("参数错误！");
        }
        task.setUpdateBy(getUsername());
        if (taskService.edit(task) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:task:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Task task)
    {
        if (StringUtils.isNull(task.getId())){
            return error("参数错误！");
        }
        if (taskService.del(task.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
