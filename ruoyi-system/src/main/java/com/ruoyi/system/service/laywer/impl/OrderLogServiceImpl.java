package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.system.domain.lawyer.OrderLog;
import com.ruoyi.system.mapper.lawyer.OrderLogMapper;
import com.ruoyi.system.service.laywer.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : OrderLogServiceImpl
 * @Description : 订单日志
 * @Author : WANGKE
 * @Date: 2023-09-01 01:20
 */
@Service
public class OrderLogServiceImpl implements OrderLogService {
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Override
    public List<OrderLog> list(OrderLog orderLog) {
        return orderLogMapper.list(orderLog);
    }

    @Override
    public OrderLog item(Long id) {
        return orderLogMapper.item(id);
    }

    @Override
    public int add(OrderLog orderLog) {
        return orderLogMapper.add(orderLog);
    }
}
