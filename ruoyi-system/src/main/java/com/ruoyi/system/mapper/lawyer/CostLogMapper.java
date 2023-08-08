package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Lawyer;

import java.util.List;

/**
 * @ClassName : CostLog
 * @Description : 积分
 * @Author : WANGKE
 * @Date: 2023-08-08 09:37
 */
public interface CostLogMapper {
    //列表
    public List<Lawyer> list(Lawyer lawyer);
    //详情
    public Lawyer item(Long id);
    //新增
    public int add(Lawyer lawyer);
    //编辑
    public int edit(Lawyer lawyer);
    //删除
    public int del(Long id);
}
