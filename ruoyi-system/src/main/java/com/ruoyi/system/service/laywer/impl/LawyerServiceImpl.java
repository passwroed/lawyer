package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.*;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.lawyer.CostLogMapper;
import com.ruoyi.system.mapper.lawyer.LawyerMapper;
import com.ruoyi.system.service.laywer.AreaService;
import com.ruoyi.system.service.laywer.CostLogService;
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
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private CostLogMapper costLogMapper;
    @Override
    public List<Lawyer> list(Lawyer lawyer) {
        if (StringUtils.isNotNull(lawyer.getPageNum()) && StringUtils.isNotNull(lawyer.getPageSize())) {
            PageHelper.startPage(lawyer.getPageNum(), lawyer.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        if (StringUtils.isNotNull(lawyer.getAreaCode())&&lawyer.getAreaCode()>0){
            String str = lawyer.getAreaCode()+"";
            int index = str.indexOf("00");

            switch (index){
                case 4:
                    lawyer.setAreaCode((int)lawyer.getAreaCode()/100);
                    break;
                case 2:
                    lawyer.setAreaCode((int)lawyer.getAreaCode()/10000);
                    break;
            }
            System.out.println("index"+lawyer.getAreaCode());
        }else {
            lawyer.setAreaCode(null);
        }
        return lawyerMapper.list(lawyer);
    }

    @Override
    public List<Lawyer> typeList(Lawyer lawyer) {
        if (StringUtils.isNotNull(lawyer.getPageNum()) && StringUtils.isNotNull(lawyer.getPageSize())) {
            PageHelper.startPage(lawyer.getPageNum(), lawyer.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return lawyerMapper.typeList(lawyer);
    }

    @Override
    public List<Lawyer> selectUserId(Lawyer lawyer) {
        if (StringUtils.isNotNull(lawyer.getPageNum()) && StringUtils.isNotNull(lawyer.getPageSize())) {
            PageHelper.startPage(lawyer.getPageNum(), lawyer.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return lawyerMapper.selectUserId(lawyer);
    }

    @Override
    public List<Lawyer> listAndCost(Lawyer lawyer) {
        if (StringUtils.isNotNull(lawyer.getPageNum()) && StringUtils.isNotNull(lawyer.getPageSize())) {
            PageHelper.startPage(lawyer.getPageNum(), lawyer.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return lawyerMapper.listAndCost(lawyer);
    }

    @Override
    public List<Lawyer> headImageList(Lawyer lawyer) {
        if (StringUtils.isNotNull(lawyer.getPageNum()) && StringUtils.isNotNull(lawyer.getPageSize())) {
            PageHelper.startPage(lawyer.getPageNum(), lawyer.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return lawyerMapper.headImageList(lawyer);
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

            if (lawyer.getStatus()<1 && status< 1){
                lawyer.setStatus(status);
                lawyer.setFeedBack(fb);
            }else if (lawyer.getStatus()<1 && status ==1) {
                //如果原始状态-1，0,提交是1加积分
                SysDictData q = sysDictDataMapper.selectDictDataById(43L);
                if (StringUtils.isNull(q)){
                    return 0;
                }
                CostLog costLog = new CostLog();
                costLog.setLawyerId(lawyer.getId());
                costLog.setCost(Double.valueOf(q.getDictValue()));
                costLog.setType(0);
                costLog.setSum(Double.valueOf(q.getDictValue()));
                costLog.setRemark("新律所福利积分");
                if (costLogMapper.add(costLog) == 0){
                    return 0;
                }
                lawyer.setStatus(status);
                lawyer.setFeedBack(fb);
            }else if (lawyer.getStatus()>=1 && status >1) {
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
