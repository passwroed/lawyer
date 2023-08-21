package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Banner;
import com.ruoyi.system.mapper.lawyer.BannerMapper;
import com.ruoyi.system.service.laywer.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : BannerServiceImpl
 * @Description : banner
 * @Author : WANGKE
 * @Date: 2023-08-16 01:07
 */
@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;
    @Override
    public List<Banner> list(Banner banner) {
        if (StringUtils.isNotNull(banner.getPageNum()) && StringUtils.isNotNull(banner.getPageSize())) {
            PageHelper.startPage(banner.getPageNum(), banner.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return bannerMapper.list(banner);
    }

    @Override
    public int add(Banner banner) {
        return bannerMapper.add(banner);
    }

    @Override
    public int edit(Banner banner) {
        return bannerMapper.edit(banner);
    }

    @Override
    public int del(Long id) {
        return bannerMapper.del(id);
    }
}
