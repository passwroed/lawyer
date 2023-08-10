package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Goods;
import com.ruoyi.system.mapper.lawyer.AreaMapper;
import com.ruoyi.system.mapper.lawyer.GoodsMapper;
import com.ruoyi.system.service.laywer.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : GoodsServiceImpl
 * @Description : 商品
 * @Author : WANGKE
 * @Date: 2023-08-01 02:15
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<Goods> list(Goods goods) {
        if (StringUtils.isNotNull(goods.getPageNum()) && StringUtils.isNotNull(goods.getPageSize())) {
            PageHelper.startPage(goods.getPageNum(), goods.getPageSize());
        }
        return goodsMapper.list(goods);
    }

    @Override
    public Goods item(Long id) {
        return goodsMapper.item(id);
    }

    @Override
    public int add(Goods goods) {
        return goodsMapper.add(goods);
    }

    @Override
    public int edit(Goods goods) {
        return goodsMapper.edit(goods);
    }

    @Override
    public int del(Long id) {
        return goodsMapper.del(id);
    }
}
