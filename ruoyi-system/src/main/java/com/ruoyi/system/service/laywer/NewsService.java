package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.News;

import java.util.List;

/**
 * @ClassName : NewsService
 * @Description : 新闻
 * @Author : WANGKE
 * @Date: 2023-07-11 17:28
 */
public interface NewsService {
    //列表
    public List<News> list(News news);
    //新增
    public int add(News news);
    //编辑
    public int edit(News news);
    //删除
    public int del(Long id);
}
