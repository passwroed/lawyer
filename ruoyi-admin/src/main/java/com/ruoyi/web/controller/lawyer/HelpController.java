package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Help;
import com.ruoyi.system.service.laywer.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : HelpController
 * @Description : 帮助中心
 * @Author : WANGKE
 * @Date: 2023-07-12 15:47
 */
@RestController
@RequestMapping("/help")
public class HelpController extends BaseController {
    @Autowired
    private HelpService helpService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:help:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Help help)
    {
        startPage();
        List<Help> list = helpService.list(help);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:help:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Help help)
    {
        help.setCreateBy(getUsername());
        if (helpService.add(help) == 0){
            return error("新增帮助中心失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:help:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Help help)
    {
        if (StringUtils.isNull(help)||StringUtils.isNull(help.getId())){
            return error("参数错误！");
        }
        help.setUpdateBy(getUsername());
        if (helpService.edit(help) == 0){
            return error("编辑帮助中心失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:help:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Help help)
    {
        if (StringUtils.isNull(help.getId())){
            return error("参数错误！");
        }
        if (helpService.del(help.getId()) == 0){
            return error("删除帮助中心失败，请联系管理员！");
        }
        return success("操作成功");
    }

}
