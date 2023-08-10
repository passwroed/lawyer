package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @ClassName : LawyerController
 * @Description : 律师
 * @Author : WANGKE
 * @Date: 2023-07-11 16:57
 */
@RestController
@RequestMapping("/lawyer")
public class LawyerController extends BaseController {
    @Autowired
    private LawyerService lawyerService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Lawyer lawyer)
    {
        startPage();
        List<Lawyer> list = lawyerService.list(lawyer);
        return getDataTable(list);
    }
    //获取中台或者地方律师（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:list')")
    @PostMapping("/typeList")
    public TableDataInfo typeList(@RequestBody Lawyer lawyer)
    {
        startPage();
        List<Lawyer> list = lawyerService.typeList(lawyer);
        return getDataTable(list);
    }
    //获取中台或者地方律师（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:list')")
    @PostMapping("/costList")
    public TableDataInfo costList(@RequestBody Lawyer lawyer)
    {
        startPage();
        List<Lawyer> list = lawyerService.listAndCost(lawyer);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated  @RequestBody Lawyer lawyer)
    {
        lawyer.setCreateBy(getUsername());
        switch (lawyerService.add(lawyer)){
            case -1:
                return error("当前手机号已绑定！");
            case 0:
                return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Lawyer lawyer)
    {
        if (StringUtils.isNull(lawyer)||StringUtils.isNull(lawyer.getId())){
            return error("参数错误！");
        }
        lawyer.setUpdateBy(getUsername());
        if (lawyerService.edit(lawyer) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑状态
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:edit')")
    @PostMapping("/status")
    public AjaxResult status(@Validated @RequestBody Lawyer lawyer)
    {
        if (StringUtils.isNull(lawyer)||StringUtils.isNull(lawyer.getId())){
            return error("参数错误！");
        }
        lawyer.setUpdateBy(getUsername());
        if (lawyerService.status(lawyer) == 0){
            return error("状态变更失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:lawyer:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Lawyer lawyer)
    {
        if (StringUtils.isNull(lawyer.getId())){
            return error("参数错误！");
        }
        if (lawyerService.del(lawyer.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
