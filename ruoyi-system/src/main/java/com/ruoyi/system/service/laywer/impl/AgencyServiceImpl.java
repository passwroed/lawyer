package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Agency;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.mapper.lawyer.AgencyMapper;
import com.ruoyi.system.mapper.lawyer.AreaMapper;
import com.ruoyi.system.service.laywer.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : AgencyServiceImpl
 * @Description : 代理
 * @Author : WANGKE
 * @Date: 2023-07-31 23:42
 */
@Service
public class AgencyServiceImpl implements AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Agency> list(Agency agency) {
        if (StringUtils.isNotNull(agency.getPageNum()) && StringUtils.isNotNull(agency.getPageSize())) {
            PageHelper.startPage(agency.getPageNum(), agency.getPageSize());
        }
        return agencyMapper.list(agency);
    }

    @Override
    public int add(Agency agency) {
        if (agency.getAreaCode()!=null && agency.getAreaCode()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(agency.getAreaCode()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            agency.setArea(name);
        }
        return agencyMapper.add(agency);
    }

    @Override
    public int edit(Agency agency) {
        if (agency.getAreaCode()!=null && agency.getAreaCode()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(agency.getAreaCode()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            agency.setArea(name);
        }
        return agencyMapper.edit(agency);
    }

    @Override
    public int del(Long id) {
        return agencyMapper.del(id);
    }
}
