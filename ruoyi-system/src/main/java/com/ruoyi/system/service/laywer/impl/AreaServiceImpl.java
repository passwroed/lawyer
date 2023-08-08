package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.mapper.lawyer.AreaMapper;
import com.ruoyi.system.service.laywer.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : AreaServiceImpl
 * @Description : 区划
 * @Author : WANGKE
 * @Date: 2023-07-18 11:34
 */
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Area> list(Area area) {
        return areaMapper.list(area);
    }

    @Override
    public int add(Area area) {
        return areaMapper.add(area);
    }

    @Override
    public Area pArea(Long pid) {
        return areaMapper.pArea(pid);
    }

    @Override
    public Area iDArea(Long id) {
        return areaMapper.iDArea(id);
    }

    @Override
    public Area Info(Long id) {
        Area area = new Area();
        area = areaMapper.iDArea(id);
        Area area1 = new Area();
        while (area.getPid()>0){
            area1 = areaMapper.iDArea((long) area.getPid());
            area1.setChild(area);
            area = area1;
        }

        return area;
    }
}
