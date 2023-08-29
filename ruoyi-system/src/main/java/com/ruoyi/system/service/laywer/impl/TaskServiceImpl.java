package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.*;
import com.ruoyi.system.mapper.lawyer.*;
import com.ruoyi.system.service.laywer.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * @ClassName : TaskServiceImpl
 * @Description : 任务
 * @Author : WANGKE
 * @Date: 2023-08-02 15:07
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private LawyerMapper lawyerMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public List<Task> list(Task task) {
        if (StringUtils.isNotNull(task.getPageNum()) && StringUtils.isNotNull(task.getPageSize())) {
            PageHelper.startPage(task.getPageNum(), task.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return taskMapper.list(task);
    }

    @Override
    public List<Task> lawyer1list(Task task) {
        if (StringUtils.isNotNull(task.getPageNum()) && StringUtils.isNotNull(task.getPageSize())) {
            PageHelper.startPage(task.getPageNum(), task.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return taskMapper.lawyer1list(task);
    }

    @Override
    public Task item(Long id) {
        return taskMapper.item(id);
    }

    @Override
    public int add(Task task) {
        if (StringUtils.isNotNull(task.getPovinceId()) && task.getPovinceId()>0){
            task.setPovince(areaMapper.iDArea(task.getPovinceId()).getName());
        }
        if (StringUtils.isNotNull(task.getCid()) && task.getCid() >0){
            task.setcName(clientMapper.item(task.getCid()).getName());
        }else {

            Client client = new Client();

            client.setPhone(task.getPhone());
            List<Client> list = clientMapper.list(client);
            if (list.size()>0){
                client = list.get(0);
            }else {
                client.setCreateBy(getUsername());
                client.setPid(getUserId());
                client.setPname(getUsername());
                client.setName(task.getcName());
                client.setPhone(task.getPhone());
                client.setAreaCode(Math.toIntExact(task.getPovinceId()));
                String name;
                Area area = areaMapper.iDArea(Long.valueOf(client.getAreaCode()));
                name = area.getName();
                while (area.getPid() > 0){
                    area = areaMapper.pArea(Long.valueOf(area.getPid()));
                    name = area.getName()+"-"+name;
                }
                client.setArea(name);
                clientMapper.headAdd(client);
            }

            task.setCid(client.getId());
            task.setcName(client.getName());
            task.setPid(client.getPid());
            task.setpName(client.getPname());
        }
        if (StringUtils.isNotNull(task.getLawyerId()) && task.getLawyerId() >0){
            Lawyer lawyer = lawyerMapper.item(task.getLawyerId());
            task.setLawyerName(lawyer.getName());
            task.setLawyerType(lawyer.getType());
        }
        if (task.getPovinceId()>0){
            String name = "";
            Area area = new Area();
            area = areaMapper.iDArea(Long.valueOf(task.getPovinceId()));
            name = area.getName();
            while (area.getPid() > 0){
                area = areaMapper.pArea(Long.valueOf(area.getPid()));
                name = area.getName()+"-"+name;
            }
            task.setPovince(name);
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        Random r=new Random();
        task.setNo(sdf.format(System.currentTimeMillis())+r.nextInt(10));//规则：时间+1位随机数
        return taskMapper.add(task);
    }

    @Override
    public int edit(Task task) {
        return taskMapper.edit(task);
    }

    @Override
    public int del(Long id) {
        Task task = taskMapper.item(id);
        if (StringUtils.isNull(task)||StringUtils.isNotNull(task.getFastLawyerId())||StringUtils.isNotNull(task.getLawyerId())){
            return 0;
        }
        Order order = new Order();
        order.setTaskNo(task.getNo());
        List<Order> list = orderMapper.list(order);
        if (list.size()>0){
            return 0;
        }
        return taskMapper.del(id);
    }

    @Override
    public int reject(Long id) {
        return taskMapper.reject(id);
    }
}
