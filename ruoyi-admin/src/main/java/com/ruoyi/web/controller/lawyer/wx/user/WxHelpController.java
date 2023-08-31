package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Help;
import com.ruoyi.system.service.laywer.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxHelpController
 * @Description : 帮助中心
 * @Author : WANGKE
 * @Date: 2023-08-30 19:51
 */
@RestController
@RequestMapping("/wxuser/help")
public class WxHelpController extends BaseController {
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
}
