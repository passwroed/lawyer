package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Html;
import com.ruoyi.system.service.laywer.HtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : HtmlController
 * @Description : HTML页面富文本
 * @Author : WANGKE
 * @Date: 2023-07-16 23:02
 */
@RestController
@RequestMapping("/html")
public class HtmlController extends BaseController {
    @Autowired
    private HtmlService htmlService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:html:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Html html)
    {
        startPage();
        List<Html> list = htmlService.list(html);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:html:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Html html)
    {
        html.setCreateBy(getUsername());
        if (htmlService.add(html) == 0){
            return error("新增富文本失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:html:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Html html)
    {
        if (StringUtils.isNull(html)||StringUtils.isNull(html.getId())){
            return error("参数错误！");
        }
        html.setUpdateBy(getUsername());
        if (htmlService.edit(html) == 0){
            return error("编辑富文本失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:html:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Html html)
    {
        if (StringUtils.isNull(html.getId())){
            return error("参数错误！");
        }
        if (htmlService.del(html.getId()) == 0){
            return error("删除富文本失败，请联系管理员！");
        }
        return success("操作成功");
    }

}
