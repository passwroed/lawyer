package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Html;
import com.ruoyi.system.mapper.lawyer.HelpMapper;
import com.ruoyi.system.mapper.lawyer.HtmlMapper;
import com.ruoyi.system.service.laywer.HelpService;
import com.ruoyi.system.service.laywer.HtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : HelpServiceImpl
 * @Description : 帮助中心
 * @Author : WANGKE
 * @Date: 2023-07-12 15:53
 */
@Service
public class HtmlServiceImpl implements HtmlService {
    @Autowired
    private HtmlMapper htmlMapper;
    @Override
    public List<Html> list(Html html) {
        if (StringUtils.isNotNull(html.getPageNum()) && StringUtils.isNotNull(html.getPageSize())) {
            PageHelper.startPage(html.getPageNum(), html.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return htmlMapper.list(html);
    }

    @Override
    public int add(Html html) {
        return htmlMapper.add(html);
    }

    @Override
    public int edit(Html html) {
        return htmlMapper.edit(html);
    }

    @Override
    public int del(Long id) {
        return htmlMapper.del(id);
    }
}
