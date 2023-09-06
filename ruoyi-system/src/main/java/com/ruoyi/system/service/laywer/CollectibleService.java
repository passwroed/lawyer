package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Collectible;

import java.util.List;

/**
 * @ClassName : CollectibleService
 * @Description : 收藏
 * @Author : WANGKE
 * @Date: 2023-09-01 03:09
 */
public interface CollectibleService {
    public List<Collectible> list(Collectible collectible);
    public Collectible item(Collectible collectible);
    //新增
    public int add(Collectible collectible);
    //删除
    public int del(Long id);
}
