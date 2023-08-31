package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.OrderLog;

import java.util.List;

/**
 * @ClassName : OrderLogMapper
 * @Description : 订单日志
 * @Author : WANGKE
 * @Date: 2023-08-31 16:16
 */
public interface OrderLogMapper {
    public List<OrderLog> list(OrderLog orderLog);
    public OrderLog item(Long id);
    //新增
    public int add(OrderLog orderLog);
}
