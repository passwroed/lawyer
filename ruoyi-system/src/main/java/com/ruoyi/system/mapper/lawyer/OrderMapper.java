package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Order;

import java.util.List;

/**
 * @ClassName : OrderMapper
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-01 14:40
 */
public interface OrderMapper {
    //列表
    public List<Order> list(Order order);
    public Order itemNo(String no);
    //详情
    public Order item(Long id);
    //新增
    public int add(Order order);
    //编辑
    public int edit(Order order);
    //删除
    public int del(Long id);
}
