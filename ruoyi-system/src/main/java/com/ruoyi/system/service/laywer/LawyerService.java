package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.domain.lawyer.Task;

import java.util.List;

/**
 * @ClassName : LawyerService
 * @Description : 律师
 * @Author : WANGKE
 * @Date: 2023-07-11 17:28
 */
public interface LawyerService {
    //列表
    public List<Lawyer> list(Lawyer lawyer);
    public List<Lawyer> typeList(Lawyer lawyer);
    //通过Userid获取详情
    public List<Lawyer> selectUserId(Lawyer lawyer);
    //详情
    public Lawyer item(Long id);
    //新增
    public int add(Lawyer lawyer);
    //编辑
    public int edit(Lawyer lawyer);
    //修改状态
    public int status(Lawyer lawyer);
    //删除
    public int del(Long id);
}
