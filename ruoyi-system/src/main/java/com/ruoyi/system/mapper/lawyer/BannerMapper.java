package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Banner;

import java.util.List;

/**
 * @ClassName : BannerMapper
 * @Description : banner
 * @Author : WANGKE
 * @Date: 2023-08-16 00:28
 */
public interface BannerMapper {
    public List<Banner> list(Banner banner);
    //新增
    public int add(Banner banner);
    //编辑
    public int edit(Banner banner);
    //删除
    public int del(Long id);
}
