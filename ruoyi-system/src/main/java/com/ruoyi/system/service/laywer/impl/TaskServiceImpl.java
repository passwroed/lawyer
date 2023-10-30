package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.TableDataInfo;
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
import java.util.TimerTask;

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
    @Autowired
    private AppealMapper appealMapper;
    @Override
    public List<Task> list(Task task) {
        if (StringUtils.isNotNull(task.getPageNum()) && StringUtils.isNotNull(task.getPageSize())) {
            PageHelper.startPage(task.getPageNum(), task.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        if (StringUtils.isNotNull(task.getPovinceId())&&task.getPovinceId()>0){
            String str = task.getPovinceId()+"";
            int index = str.indexOf("00");

            switch (index){
                case 4:
                    task.setPovinceId(task.getPovinceId()/100);
                    break;
                case 2:
                    task.setPovinceId(task.getPovinceId()/10000);
                    break;
            }
            System.out.println("index"+task.getPovinceId());
        }else {
            task.setPovinceId(null);
        }
        return taskMapper.list(task);
    }

    @Override
    public TableDataInfo listToPassword(Task task) {
        if (StringUtils.isNotNull(task.getPageNum()) && StringUtils.isNotNull(task.getPageSize())) {
            PageHelper.startPage(task.getPageNum(), task.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        if (StringUtils.isNotNull(task.getPovinceId())&&task.getPovinceId()>0){
            String str = task.getPovinceId()+"";
            int index = str.indexOf("00");

            switch (index){
                case 4:
                    task.setPovinceId(task.getPovinceId()/100);
                    break;
                case 2:
                    task.setPovinceId(task.getPovinceId()/10000);
                    break;
            }
            System.out.println("index"+task.getPovinceId());
        }else {
            task.setPovinceId(null);
        }
        List<Task> list = taskMapper.list(task);
        List<Task> returnlist = new ArrayList<>();
        if (list.size() > 0) {
            for (Task task1 : list) {
                if (StringUtils.isNotNull(task1.getPhone())) {
                    task1.setPhone(task1.getPhone().replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2"));
                    returnlist.add(task1);
                }
            }
        } else {
            returnlist = list;
        }
        TableDataInfo info = new TableDataInfo(returnlist, (int) new PageInfo(list).getTotal());
        info.setCode(HttpStatus.SUCCESS);
        info.setMsg("查询成功");
        return info;
    }

    @Override
    public List<Task> lawyer1list(Task task) {
        if (StringUtils.isNotNull(task.getPageNum()) && StringUtils.isNotNull(task.getPageSize())) {
            PageHelper.startPage(task.getPageNum(), task.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        if (StringUtils.isNotNull(task.getPovinceId())&&task.getPovinceId()>0){
            String str = task.getPovinceId()+"";
            int index = str.indexOf("00");

            switch (index){
                case 4:
                    task.setPovinceId((int)task.getPovinceId()/100);
                    break;
                case 2:
                    task.setPovinceId((int)task.getPovinceId()/10000);
                    break;
            }
            System.out.println("index"+task.getPovinceId());
        }else {
            task.setPovinceId(null);
        }
        return taskMapper.lawyer1list(task);
    }

    @Override
    public Task item(Long id) {
        return taskMapper.item(id);
    }

    @Override
    public Task itemNo(String no) {
        return taskMapper.itemNo(no);
    }

    @Override
    public Task itemOrderNo(String orderNo) {
        return taskMapper.itemOrderNo(orderNo);
    }

    @Override
    public int add(Task task) {
        if (StringUtils.isNotNull(task.getPovinceId()) && task.getPovinceId()>0){
            task.setPovince(areaMapper.iDArea(Long.valueOf(task.getPovinceId())).getName());
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

        if (StringUtils.isNotNull(task.getPovinceId())&&task.getPovinceId()>0){
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
        return taskMapper.edit(task);
    }

    @Override
    public int editStatus0(Task task) {
        return taskMapper.editStatus0(task);
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
