package com.ruoyi.system.service.laywer;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : StatisticsService
 * @Description : 统计
 * @Author : WANGKE
 * @Date: 2023-09-10 02:08
 */
public interface StatisticsService {
    public List<Map> midLawyer(Map<String,Object> map);
    public List<Map> orderMoney(Map<String,Object> map);
    public List<Map> orderNum(Map<String,Object> map);
    public List<Map> typeTop(Map<String,Object> map);
}
