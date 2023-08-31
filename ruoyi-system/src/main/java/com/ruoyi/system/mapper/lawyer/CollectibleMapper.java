package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Collectible;

import java.util.List;

/**
 * @ClassName : CollectibleMapper
 * @Description : 收藏
 * @Author : WANGKE
 * @Date: 2023-09-01 02:57
 */
public interface CollectibleMapper {
    public List<Collectible> list(Collectible collectible);
    //新增
    public int add(Collectible collectible);
    //删除
    public int del(Long id);
}
