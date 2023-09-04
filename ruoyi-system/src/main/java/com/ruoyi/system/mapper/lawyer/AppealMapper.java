package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Appeal;

import java.util.List;

/**
 * @ClassName : AppealMapper
 * @Description : 述求
 * @Author : WANGKE
 * @Date: 2023-09-03 12:36
 */
public interface AppealMapper {

    public List<Appeal> list(Appeal appeal);
    public Appeal item(Long id);
    //新增
    public int add(Appeal appeal);
    //编辑
    public int edit(Appeal appeal);
    //删除
    public int del(Long id);
    public int isNotStatus(Long cid);
    public int isStatus(Long id);
}
