package com.ruoyi.system.mapper.lawyer;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : StatisticsMapper
 * @Description : 统计
 * @Author : WANGKE
 * @Date: 2023-09-10 02:03
 */
public interface StatisticsMapper {
    public List<Map> midLawyer(Map<String,Object> map);
    public List<Map> orderMoney(Map<String,Object> map);
    public List<Map> orderNum(Map<String,Object> map);
    public List<Map> typeTop(Map<String,Object> map);
}
