package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Collectible;
import com.ruoyi.system.mapper.lawyer.CollectibleMapper;
import com.ruoyi.system.service.laywer.CollectibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : CollectibleServiceImpl
 * @Description : 收藏
 * @Author : WANGKE
 * @Date: 2023-09-01 03:09
 */
@Service
public class CollectibleServiceImpl implements CollectibleService {
    @Autowired
    private CollectibleMapper collectibleMapper;
    @Override
    public List<Collectible> list(Collectible collectible) {
        if (StringUtils.isNotNull(collectible.getPageNum()) && StringUtils.isNotNull(collectible.getPageSize())) {
            PageHelper.startPage(collectible.getPageNum(), collectible.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return collectibleMapper.list(collectible);
    }

    @Override
    public int add(Collectible collectible) {
        return collectibleMapper.add(collectible);
    }

    @Override
    public int del(Long id) {
        return collectibleMapper.del(id);
    }
}
