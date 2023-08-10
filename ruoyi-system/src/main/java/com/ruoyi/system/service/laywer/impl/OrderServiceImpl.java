package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.mapper.lawyer.OrderMapper;
import com.ruoyi.system.service.laywer.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

/**
 * @ClassName : OrderServiceImpl
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-02 00:54
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public List<Order> list(Order order) {
        if (StringUtils.isNotNull(order.getPageNum()) && StringUtils.isNotNull(order.getPageSize())) {
            PageHelper.startPage(order.getPageNum(), order.getPageSize());
        }
        return orderMapper.list(order);
    }

    @Override
    public Order item(Long id) {
        return orderMapper.item(id);
    }

    @Override
    public int add(Order order) {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        Random r=new Random();
        order.setNo(sdf.format(System.currentTimeMillis())+r.nextInt(10));//规则：时间+1位随机数
        return orderMapper.add(order);
    }

    @Override
    public int edit(Order order) {
        return orderMapper.edit(order);
    }

    @Override
    public int del(Long id) {
        return orderMapper.del(id);
    }
}
