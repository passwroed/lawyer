package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.service.laywer.CostLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : CostLogController
 * @Description : 积分
 * @Author : WANGKE
 * @Date: 2023-08-08 10:42
 */
@RestController
@RequestMapping("/costLog")
public class CostLogController extends BaseController {
    @Autowired
    private CostLogService costLogService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:costLog:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody CostLog costLog)
    {
        startPage();
        List<CostLog> list = costLogService.list(costLog);
        return getDataTable(list);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody CostLog costLog)
    {
        return success(costLogService.item(costLog.getId()));
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:costLog:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody CostLog costLog)
    {
        costLog.setCreateBy(getUsername());
        if (costLogService.add(costLog) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:costLog:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody CostLog costLog)
    {
        if (StringUtils.isNull(costLog)||StringUtils.isNull(costLog.getId())){
            return error("参数错误！");
        }
        costLog.setUpdateBy(getUsername());
        if (costLogService.edit(costLog) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:costLog:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody CostLog costLog)
    {
        if (StringUtils.isNull(costLog.getId())){
            return error("参数错误！");
        }
        if (costLogService.del(costLog.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
