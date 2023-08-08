package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Agency;
import com.ruoyi.system.service.laywer.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : AgencyController
 * @Description : 代理
 * @Author : WANGKE
 * @Date: 2023-07-31 23:43
 */
@RestController
@RequestMapping("/agency")
public class AgencyController extends BaseController {
    @Autowired
    private AgencyService agencyService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:agency:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Agency agency)
    {
        startPage();
        List<Agency> list = agencyService.list(agency);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:agency:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Agency agency)
    {
        agency.setCreateBy(getUsername());
        if (agencyService.add(agency) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:agency:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Agency agency)
    {
        if (StringUtils.isNull(agency)||StringUtils.isNull(agency.getId())){
            return error("参数错误！");
        }
        agency.setUpdateBy(getUsername());
        if (agencyService.edit(agency) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:agency:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Agency agency)
    {
        if (StringUtils.isNull(agency.getId())){
            return error("参数错误！");
        }
        if (agencyService.del(agency.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
