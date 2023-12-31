package com.ruoyi.system.mapper.lawyer;


import com.ruoyi.system.domain.lawyer.CostLog;

import java.util.List;

/**
 * @ClassName : CostLog
 * @Description : 积分
 * @Author : WANGKE
 * @Date: 2023-08-08 09:37
 */
public interface CostLogMapper {
    //列表
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
