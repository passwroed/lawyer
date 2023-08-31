package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.service.laywer.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxNewsController
 * @Description : 微信用户新闻
 * @Author : WANGKE
 * @Date: 2023-08-30 18:06
 */
@RestController
@RequestMapping("/wxuser/news")
public class WxNewsController extends BaseController {
    @Autowired
    private NewsService newsService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:news:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody News news)
    {
        startPage();
        List<News> list = newsService.list(news);
        return getDataTable(list);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody News news)
    {
        return success(newsService.item(news.getId()));
    }
}
