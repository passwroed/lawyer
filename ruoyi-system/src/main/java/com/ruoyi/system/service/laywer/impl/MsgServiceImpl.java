package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Msg;
import com.ruoyi.system.mapper.lawyer.MsgMapper;
import com.ruoyi.system.service.laywer.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : MsgServiceImpl
 * @Description : 站内消息
 * @Author : WANGKE
 * @Date: 2023-09-01 10:28
 */
@Service
public class MsgServiceImpl implements MsgService {
    @Autowired
    private MsgMapper msgMapper;
    @Override
    public List<Msg> list(Msg msg) {
        if (StringUtils.isNotNull(msg.getPageNum()) && StringUtils.isNotNull(msg.getPageSize())) {
            PageHelper.startPage(msg.getPageNum(), msg.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return msgMapper.list(msg);
    }

    @Override
    public int add(Msg msg) {
        return msgMapper.add(msg);
    }

    @Override
    public int edit(Msg msg) {
        return msgMapper.edit(msg);
    }

    @Override
    public int del(Long id) {
        return msgMapper.del(id);
    }
}
