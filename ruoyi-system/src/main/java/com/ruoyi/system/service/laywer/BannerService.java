package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Banner;

import java.util.List;

/**
 * @ClassName : BannerService
 * @Description : banner
 * @Author : WANGKE
 * @Date: 2023-08-16 01:06
 */
public interface BannerService {
    public List<Banner> list(Banner banner);
    //新增
    public int add(Banner banner);
    //编辑
    public int edit(Banner banner);
    //删除
    public int del(Long id);
}
