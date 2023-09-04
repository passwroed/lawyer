package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Appeal;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.mapper.lawyer.AppealMapper;
import com.ruoyi.system.mapper.lawyer.AreaMapper;
import com.ruoyi.system.service.laywer.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : AppealServiceImpl
 * @Description : 述求
 * @Author : WANGKE
 * @Date: 2023-09-03 12:37
 */
@Service
public class AppealServiceImpl implements AppealService {
    @Autowired
    private AppealMapper appealMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Appeal> list(Appeal appeal) {
        if (StringUtils.isNotNull(appeal.getPageNum()) && StringUtils.isNotNull(appeal.getPageSize())) {
            PageHelper.startPage(appeal.getPageNum(), appeal.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return appealMapper.list(appeal);
    }

    @Override
    public Appeal item(Long id) {
        return appealMapper.item(id);
    }

    @Override
    public int add(Appeal appeal) {
        if (appeal.getPovinceId()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(appeal.getPovinceId()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            appeal.setPovince(name);
        }
        if (StringUtils.isNotNull(appeal.getStatus()) && appeal.getStatus() == 1){
            isNotStatus(appeal.getCid());
        }else {
            Appeal appeal1 = new Appeal();
            appeal1.setCid(appeal.getCid());
            if (list(appeal1).size()==0){
                appeal.setStatus(1);
            }
        }

        return appealMapper.add(appeal);
    }

    @Override
    public int edit(Appeal appeal) {
        if (appeal.getPovinceId()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(appeal.getPovinceId()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            appeal.setPovince(name);
        }
        if (StringUtils.isNotNull(appeal.getStatus()) && appeal.getStatus() == 1){
            isNotStatus(appeal.getCid());
        }
        return appealMapper.edit(appeal);
    }

    @Override
    public int del(Long id) {
        return appealMapper.del(id);
    }

    @Override
    public int isNotStatus(Long cid) {
        return appealMapper.isNotStatus(cid);
    }

    @Override
    public int isStatus(Long id) {
        return appealMapper.isStatus(id);
    }
}
