package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Html;

import java.util.List;

/**
 * @ClassName : AreaService
 * @Description : 区划
 * @Author : WANGKE
 * @Date: 2023-07-12 15:53
 */
public interface AreaService {
    //列表
    public List<Area> list(Area area);
    //新增
    public int add(Area area);

    //查询上级位置
    public Area pArea(Long pid);
    public Area iDArea(Long id);
    public Area Info(Long id);
}
