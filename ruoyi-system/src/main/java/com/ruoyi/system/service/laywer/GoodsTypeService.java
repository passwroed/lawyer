package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.GoodsType;

import java.util.List;

/**
 * @ClassName : GoodsTypeService
 * @Description : 商品类型
 * @Author : WANGKE
 * @Date: 2023-08-18 14:14
 */
public interface GoodsTypeService {
    //列表
    public List<GoodsType> list(GoodsType goodsType);
    //新增
    public int add(GoodsType goodsType);
    //编辑
    public int edit(GoodsType goodsType);
    //删除
    public int del(Long id);
}
