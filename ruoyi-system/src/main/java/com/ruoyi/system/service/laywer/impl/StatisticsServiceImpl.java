package com.ruoyi.system.service.laywer.impl;

import com.ruoyi.system.mapper.lawyer.StatisticsMapper;
import com.ruoyi.system.service.laywer.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : StatisticsServiceImpl
 * @Description : 统计
 * @Author : WANGKE
 * @Date: 2023-09-10 02:09
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Override
    public List<Map> midLawyer(Map<String,Object> map) {
        return statisticsMapper.midLawyer(map);
    }

    @Override
    public List<Map> orderMoney(Map<String,Object> map) {
        return statisticsMapper.orderMoney(map);
    }

    @Override
    public List<Map> typeTop(Map<String,Object> map) {
        return statisticsMapper.typeTop(map);
    }

    @Override
    public List<Map> orderNumDay() {
        List<Map> list = new ArrayList<>();
        Map now = statisticsMapper.orderNumNow();
        now.put("date","now");
        Map yd = statisticsMapper.orderNumYd();
        yd.put("date","yd");
        list.add(now);
        list.add(yd);
        return list;
    }

    @Override
    public List<Map> userNumDay() {
        List<Map> list = new ArrayList<>();
        Map now = statisticsMapper.userNumNow();
        now.put("date","now");
        Map yd = statisticsMapper.userNumYd();
        yd.put("date","yd");
        list.add(now);
        list.add(yd);
        return list;
    }

    @Override
    public List<Map> orderAllNumDay() {
        List<Map> list = new ArrayList<>();
        Map now = statisticsMapper.orderAllNumNow();
        now.put("date","now");
        Map yd = statisticsMapper.orderAllNumYd();
        yd.put("date","yd");
        list.add(now);
        list.add(yd);
        return list;
    }

    @Override
    public List<Map> orderNum(Map<String,Object> map) {
        return statisticsMapper.orderNum(map);
    }

}
