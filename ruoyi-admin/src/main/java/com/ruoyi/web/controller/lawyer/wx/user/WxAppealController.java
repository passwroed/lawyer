package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Appeal;
import com.ruoyi.system.service.laywer.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : AppealController
 * @Description : 述求
 * @Author : WANGKE
 * @Date: 2023-09-03 12:45
 */
@RestController
@RequestMapping("/wxuser/appeal")
public class WxAppealController extends BaseController {
    @Autowired
    private AppealService appealService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:appeal:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Appeal appeal)
    {
        startPage();
        appeal.setCid(getUserId());
        List<Appeal> list = appealService.list(appeal);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:appeal:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Appeal appeal)
    {
        appeal.setCid(getUserId());
        appeal.setCreateBy(getUsername());
        if (appealService.add(appeal) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:appeal:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Appeal appeal)
    {
        if (StringUtils.isNull(appeal)||StringUtils.isNull(appeal.getId())){
            return error("参数错误！");
        }
        appeal.setCid(getUserId());
        appeal.setUpdateBy(getUsername());
        if (appealService.edit(appeal) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:appeal:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Appeal appeal)
    {
        if (StringUtils.isNull(appeal.getId())){
            return error("参数错误！");
        }
        if (appealService.del(appeal.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
