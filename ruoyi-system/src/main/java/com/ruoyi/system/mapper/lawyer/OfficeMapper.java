package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.domain.lawyer.Office;

import java.util.List;

/**
 * @ClassName : OfficeMapper
 * @Description : 律所
 * @Author : WANGKE
 * @Date: 2023-07-20 15:06
 */
public interface OfficeMapper {
    //列表
    public List<Office> list(Office office);
    //新增
    public int add(Office office);
    //编辑
    public int edit(Office office);
    //删除
    public int del(Long id);
}
