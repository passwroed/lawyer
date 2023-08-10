package com.ruoyi.web.controller.lawyer;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Office;
import com.ruoyi.system.service.laywer.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : OfficeController
 * @Description : 律所
 * @Author : WANGKE
 * @Date: 2023-07-19 14:47
 */
@RestController
@RequestMapping("/office")
public class OfficeController extends BaseController {
    @Autowired
    private OfficeService officeService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:office:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Office office)
    {
        startPage();
        List<Office> list = officeService.list(office);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:office:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Office office)
    {
        office.setCreateBy(getUsername());
        if (officeService.add(office) == 0){
            return error("新增新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:office:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Office office)
    {
        if (StringUtils.isNull(office)||StringUtils.isNull(office.getId())){
            return error("参数错误！");
        }
        office.setUpdateBy(getUsername());
        if (officeService.edit(office) == 0){
            return error("编辑新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:office:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Office office)
    {
        if (StringUtils.isNull(office.getId())){
            return error("参数错误！");
        }
        if (officeService.del(office.getId()) == 0){
            return error("删除新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
