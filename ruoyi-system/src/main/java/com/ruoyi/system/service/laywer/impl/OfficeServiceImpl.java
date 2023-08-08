package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Office;
import com.ruoyi.system.mapper.lawyer.AreaMapper;
import com.ruoyi.system.mapper.lawyer.OfficeMapper;
import com.ruoyi.system.service.laywer.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : OfficeServiceImpl
 * @Description : 律所
 * @Author : WANGKE
 * @Date: 2023-07-20 17:19
 */
@Service
public class OfficeServiceImpl implements OfficeService {
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Office> list(Office office) {
        return officeMapper.list(office);
    }

    @Override
    public int add(Office office) {
        if (office.getAreaCode()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(office.getAreaCode()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            office.setArea(name);
        }
        return officeMapper.add(office);
    }

    @Override
    public int edit(Office office) {
        if (office.getAreaCode()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(office.getAreaCode()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            office.setArea(name);
        }
        return officeMapper.edit(office);
    }

    @Override
    public int del(Long id) {
        return officeMapper.del(id);
    }
}
