package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.system.domain.lawyer.Help;
import com.ruoyi.system.mapper.lawyer.HelpMapper;
import com.ruoyi.system.service.laywer.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : HelpServiceImpl
 * @Description : 帮助中心
 * @Author : WANGKE
 * @Date: 2023-07-12 15:53
 */
@Service
public class HelpServiceImpl implements HelpService {
    @Autowired
    private HelpMapper helpMapper;
    @Override
    public List<Help> list(Help help) {
        return helpMapper.list(help);
    }

    @Override
    public int add(Help help) {
        return helpMapper.add(help);
    }

    @Override
    public int edit(Help help) {
        return helpMapper.edit(help);
    }

    @Override
    public int del(Long id) {
        return helpMapper.del(id);
    }
}
