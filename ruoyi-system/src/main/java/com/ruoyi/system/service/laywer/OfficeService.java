package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.News;
import com.ruoyi.system.domain.lawyer.Office;

import java.util.List;

/**
 * @ClassName : OfficeService
 * @Description : 律所
 * @Author : WANGKE
 * @Date: 2023-07-20 17:18
 */
public interface OfficeService {

    //列表
    public List<Office> list(Office office);
    //新增
    public int add(Office office);
    //编辑
    public int edit(Office office);
    //删除
    public int del(Long id);
}
