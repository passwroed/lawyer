package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.OrderLog;

import java.util.List;

/**
 * @ClassName : OrderLogService
 * @Description : 订单日志
 * @Author : WANGKE
 * @Date: 2023-09-01 01:19
 */
public interface OrderLogService {
    public List<OrderLog> list(OrderLog orderLog);
    public OrderLog item(Long id);
    //新增
    public int add(OrderLog orderLog);
}
