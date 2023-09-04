package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.service.laywer.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxAreaController
 * @Description : 区划
 * @Author : WANGKE
 * @Date: 2023-09-03 00:03
 */
@RestController
@RequestMapping("/wxuser/area")
public class WxAreaController extends BaseController {
    @Autowired
    private AreaService areaService;

    @Anonymous
    @PostMapping("/list")
    public TableDataInfo list(@Validated @RequestBody Area area)
    {
//        startPage();
        List<Area> list = areaService.list(area);
        return getDataTable(list);
    }
    @PostMapping("/info")
    @Anonymous
    public AjaxResult Info(@RequestBody Area area)
    {
        return success(areaService.Info(Long.valueOf(area.getId())));
    }
}
