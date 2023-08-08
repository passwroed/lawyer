package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Help;

import java.util.List;

/**
 * @ClassName : HelpMapper.xml
 * @Description : 帮助中心
 * @Author : WANGKE
 * @Date: 2023-07-12 15:51
 */
public interface HelpMapper {
    //列表
    public List<Help> list(Help help);
    //新增
    public int add(Help help);
    //编辑
    public int edit(Help help);
    //删除
    public int del(Long id);
}
