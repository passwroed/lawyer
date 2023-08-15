package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.mapper.lawyer.NewsMapper;
import com.ruoyi.system.service.laywer.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : NewsServiceImpl
 * @Description :新闻
 * @Author : WANGKE
 * @Date: 2023-07-11 17:28
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public List<News> list(News news) {
        if (StringUtils.isNotNull(news.getPageNum()) && StringUtils.isNotNull(news.getPageSize())) {
            PageHelper.startPage(news.getPageNum(), news.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return newsMapper.list(news);
    }

    @Override
    public int add(News news) {
        return newsMapper.add(news);
    }

    @Override
    public int edit(News news) {
        return newsMapper.edit(news);
    }

    @Override
    public int del(Long id) {
        return newsMapper.del(id);
    }
}
