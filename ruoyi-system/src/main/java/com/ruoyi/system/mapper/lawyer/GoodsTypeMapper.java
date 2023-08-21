package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.GoodsType;

import java.util.List;

/**
 * @ClassName : GoodsTypeMapper
 * @Description : 商品类型
 * @Author : WANGKE
 * @Date: 2023-08-18 14:08
 */
public interface GoodsTypeMapper {
    //列表
    public List<GoodsType> list(GoodsType goodsType);
    //新增
    public int add(GoodsType goodsType);
    //编辑
    public int edit(GoodsType goodsType);
    //删除
    public int del(Long id);
}
