package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Msg;
import com.ruoyi.system.domain.lawyer.News;

import java.util.List;

/**
 * @ClassName : MsgMapper
 * @Description : 站内消息
 * @Author : WANGKE
 * @Date: 2023-09-01 10:28
 */
public interface MsgMapper {
    public List<Msg> list(Msg msg);
    //新增
    public int add(Msg msg);
    public int edit(Msg msg);
    //删除
    public int del(Long id);
}
