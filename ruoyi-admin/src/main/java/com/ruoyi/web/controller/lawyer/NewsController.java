package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.service.laywer.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName : NewsController
 * @Description : 新闻
 * @Author : WANGKE
 * @Date: 2023-07-11 16:57
 */
@RestController
@RequestMapping("/news")
public class NewsController extends BaseController {
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
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:news:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated  @RequestBody News news)
    {
        news.setCreateBy(getUsername());
        if (newsService.add(news) == 0){
            return error("新增新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:news:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody News news)
    {
        if (StringUtils.isNull(news)||StringUtils.isNull(news.getId())){
            return error("参数错误！");
        }
        news.setUpdateBy(getUsername());
        if (newsService.edit(news) == 0){
            return error("编辑新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:news:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody News news)
    {
        if (StringUtils.isNull(news.getId())){
            return error("参数错误！");
        }
        if (newsService.del(news.getId()) == 0){
            return error("删除新闻失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
