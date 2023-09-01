package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Banner;
import com.ruoyi.system.service.laywer.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : BannerController
 * @Description : banner
 * @Author : WANGKE
 * @Date: 2023-09-01 16:11
 */
@RestController
@RequestMapping("/wxuser/banner")
public class WxBannerController extends BaseController {
    @Autowired
    private BannerService bannerService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:banner:list')")
    @Anonymous
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Banner banner) {
        startPage();
        List<Banner> list = bannerService.list(banner);
        return getDataTable(list);
    }
}
