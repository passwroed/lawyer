package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Banner;
import com.ruoyi.system.service.laywer.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : BannerController
 * @Description : banner
 * @Author : WANGKE
 * @Date: 2023-08-16 09:20
 */
@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {
    @Autowired
    private BannerService bannerService;
    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:banner:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Banner banner)
    {
        startPage();
        List<Banner> list = bannerService.list(banner);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:banner:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Banner banner)
    {
        banner.setCreateBy(getUsername());
        if (bannerService.add(banner) == 0){
            return error("新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:banner:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Banner banner)
    {
        if (StringUtils.isNull(banner)||StringUtils.isNull(banner.getId())){
            return error("参数错误！");
        }
        banner.setUpdateBy(getUsername());
        if (bannerService.edit(banner) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:banner:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Banner banner)
    {
        if (StringUtils.isNull(banner.getId())){
            return error("参数错误！");
        }
        if (bannerService.del(banner.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
