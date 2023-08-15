package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.mapper.lawyer.AreaMapper;
import com.ruoyi.system.mapper.lawyer.ClientMapper;
import com.ruoyi.system.service.laywer.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : ClinetServiceImpl
 * @Description : 代理
 * @Author : WANGKE
 * @Date: 2023-08-01 01:09
 */
@Service
public class ClinetServiceImpl implements ClientService {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Client> list(Client client) {
        if (StringUtils.isNotNull(client.getPageNum()) && StringUtils.isNotNull(client.getPageSize())) {
            PageHelper.startPage(client.getPageNum(), client.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return clientMapper.list(client);
    }

    @Override
    public Client item(Long id) {
        return clientMapper.item(id);
    }

    @Override
    public int add(Client client) {
        if (client.getAreaCode()!=null && client.getAreaCode()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(client.getAreaCode()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            client.setArea(name);
        }
        return clientMapper.add(client);
    }

    @Override
    public int headAdd(Client client) {
        return clientMapper.headAdd(client);
    }


    @Override
    public int edit(Client client) {
        if (client.getAreaCode()!=null && client.getAreaCode()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(client.getAreaCode()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            client.setArea(name);
        }
        return clientMapper.edit(client);
    }

    @Override
    public int del(Long id) {
        return clientMapper.del(id);
    }
}
