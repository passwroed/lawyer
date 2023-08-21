package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.system.domain.lawyer.GoodsType;
import com.ruoyi.system.mapper.lawyer.GoodsTypeMapper;
import com.ruoyi.system.service.laywer.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : GoodsTypeServiceImpl
 * @Description : 商品类型
 * @Author : WANGKE
 * @Date: 2023-08-18 14:15
 */
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;
    @Override
    public List<GoodsType> list(GoodsType goodsType) {
        return goodsTypeMapper.list(goodsType);
    }

    @Override
    public int add(GoodsType goodsType) {
        return goodsTypeMapper.add(goodsType);
    }

    @Override
    public int edit(GoodsType goodsType) {
        return goodsTypeMapper.edit(goodsType);
    }

    @Override
    public int del(Long id) {
        return goodsTypeMapper.del(id);
    }
}
