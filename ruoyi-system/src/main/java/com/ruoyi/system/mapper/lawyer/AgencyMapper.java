package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Agency;

import java.util.List;

/**
 * @ClassName : Agency
 * @Description : 代理
 * @Author : WANGKE
 * @Date: 2023-07-31 23:28
 */
public interface AgencyMapper {

    //列表
    public List<Agency> list(Agency agency);
    //新增
    public int add(Agency agency);
    //编辑
    public int edit(Agency agency);
    //删除
    public int del(Long id);
}
