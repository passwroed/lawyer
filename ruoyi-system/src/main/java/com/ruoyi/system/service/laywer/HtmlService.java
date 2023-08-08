package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Html;

import java.util.List;

/**
 * @ClassName : HtmlService
 * @Description : 富文本`
 * @Author : WANGKE
 * @Date: 2023-07-12 15:53
 */
public interface HtmlService {
    //列表
    public List<Html> list(Html html);
    //新增
    public int add(Html html);
    //编辑
    public int edit(Html html);
    //删除
    public int del(Long id);
}
