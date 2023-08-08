package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Office;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.mapper.lawyer.LawyerMapper;
import com.ruoyi.system.service.laywer.AreaService;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : LawyerServiceImpl
 * @Description : 律师
 * @Author : WANGKE
 * @Date: 2023-07-23 01:07
 */
@Service
public class LawyerServiceImpl implements LawyerService {
    @Autowired
    private LawyerMapper lawyerMapper;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AreaService areaService;
    @Override
    public List<Lawyer> list(Lawyer lawyer) {

        return lawyerMapper.list(lawyer);
    }

    @Override
    public List<Lawyer> typeList(Lawyer lawyer) {
        return lawyerMapper.typeList(lawyer);
    }

    @Override
    public List<Lawyer> selectUserId(Lawyer lawyer) {
        return lawyerMapper.selectUserId(lawyer);
    }

    @Override
    public Lawyer item(Long id) {
        return lawyerMapper.item(id);
    }

    @Override
    public int add(Lawyer lawyer) {
        List<Lawyer> list = lawyerMapper.list(new Lawyer(lawyer.getPhone()));
        if (list.size() > 0) {
            return -1;
        }
        Office office = new Office();
        office.setId(lawyer.getOfficeId());
        List<Office> officeList = officeService.list(office);
        if (officeList.size() >0){
            office = officeList.get(0);
            lawyer.setOfficeName(office.getName());
        }else {
            return 0;
        }
        Area area = new Area();
        area = areaService.iDArea(Long.valueOf(lawyer.getAreaCode()));
        if (area.getPid()>=0){
            String name = "";
            name = area.getName();
            while (area.getPid() > 0){
                area = areaService.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            lawyer.setArea(name);
        }else {
            return 0;
        }
        return lawyerMapper.add(lawyer);
    }

    @Override
    public int edit(Lawyer lawyer) {
        Office office = new Office();
        office.setId(lawyer.getOfficeId());
        List<Office> officeList = officeService.list(office);
        if (officeList.size() >0){
            office = officeList.get(0);
            lawyer.setOfficeName(office.getName());
        }else {
            return 0;
        }
        Area area = new Area();
        area = areaService.pArea(Long.valueOf(lawyer.getAreaCode()));
        if (area.getPid()>=0){
            String name = "";
            name = area.getName();
            while (area.getPid() > 0){
                area = areaService.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            lawyer.setArea(name);
        }else {
            return 0;
        }
        return lawyerMapper.edit(lawyer);
    }

    @Override
    public int status(Lawyer lawyer) {
        int status = lawyer.getStatus();
        String fb = lawyer.getFeedBack();
        Lawyer lawyer1 = new Lawyer();
        lawyer1.setId(lawyer.getId());
        List<Lawyer> list = lawyerMapper.list(lawyer1);
        if (list.size() == 1){
            lawyer = list.get(0);

            if (lawyer.getStatus()<1 && status<= 1){
                lawyer.setStatus(status);
                lawyer.setFeedBack(fb);
            } else if (lawyer.getStatus()>=1 && status >1) {
                lawyer.setStatus(status);
            } else {
                return 0;
            }
        }else {
            return 0;
        }
        return lawyerMapper.status(lawyer);
    }

    @Override
    public int del(Long id) {
        return lawyerMapper.del(id);
    }
}
