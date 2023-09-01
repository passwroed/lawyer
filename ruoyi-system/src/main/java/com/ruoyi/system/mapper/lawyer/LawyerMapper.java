package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.domain.lawyer.Order;

import java.util.List;

/**
 * @ClassName : LawyerMapper
 * @Description : 律师
 * @Author : WANGKE
 * @Date: 2023-07-11 17:45
 */
public interface LawyerMapper {
    //列表
    public List<Lawyer> list(Lawyer lawyer);
    //详情
    public Lawyer item(Long id);
    public List<Lawyer> selectUserId(Lawyer lawyer);
    public List<Lawyer> listAndCost(Lawyer lawyer);
    public List<Lawyer> typeList(Lawyer lawyer);
    public List<Lawyer> headImageList(Lawyer lawyer);
    //新增
    public int add(Lawyer lawyer);
    //编辑
    public int edit(Lawyer lawyer);
    //修改状态
    public int status(Lawyer lawyer);
    //删除
    public int del(Long id);
}
