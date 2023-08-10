package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.CostLog;

import java.util.List;

/**
 * @ClassName : ClostLogService
 * @Description : 积分
 * @Author : WANGKE
 * @Date: 2023-08-08 10:36
 */
public interface CostLogService {
    public List<CostLog> list(CostLog costLog);
    //详情
    public CostLog item(Long id);
    public CostLog newCostLog(Long id);
    //新增
    public int add(CostLog costLog);
    //编辑
    public int edit(CostLog costLog);
    //删除
    public int del(Long id);
}
