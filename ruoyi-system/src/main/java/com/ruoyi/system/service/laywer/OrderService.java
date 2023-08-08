package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Order;

import java.util.List;

/**
 * @ClassName : OrderService
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-02 00:53
 */
public interface OrderService {
    //列表
    public List<Order> list(Order order);
    //详情
    public Order item(Long id);
    //新增
    public int add(Order order);
    //编辑
    public int edit(Order order);
    //删除
    public int del(Long id);
}
