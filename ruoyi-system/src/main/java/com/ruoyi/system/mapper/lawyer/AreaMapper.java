package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Html;

import java.util.List;

/**
 * @ClassName : HtmlMapper.xml
 * @Description : 富文本框
 * @Author : WANGKE
 * @Date: 2023-07-12 15:51
 */
public interface AreaMapper {
    //列表
    public List<Area> list(Area area);
    //查询上级位置
    public Area pArea(Long pid);
    public Area iDArea(Long id);
    //新增
    public int add(Area area);
}
