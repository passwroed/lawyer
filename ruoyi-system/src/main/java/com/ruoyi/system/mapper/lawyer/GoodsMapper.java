package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Goods;

import java.util.List;

/**
 * @ClassName : GoodsMapper
 * @Description : 商品
 * @Author : WANGKE
 * @Date: 2023-08-01 02:13
 */
public interface GoodsMapper {
    //列表
    public List<Goods> list(Goods goods);
    //详情
    public Goods item(Long id);
    //新增
    public int add(Goods goods);
    //编辑
    public int edit(Goods goods);
    //删除
    public int del(Long id);
}
