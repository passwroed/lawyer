package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.mapper.lawyer.CostLogMapper;
import com.ruoyi.system.service.laywer.CostLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : CostLogServiceImpl
 * @Description : 积分
 * @Author : WANGKE
 * @Date: 2023-08-08 10:37
 */
@Service
public class CostLogServiceImpl implements CostLogService {
    @Autowired
    private CostLogMapper costLogMapper;
    @Override
    public List<CostLog> list(CostLog costLog) {
        if (StringUtils.isNotNull(costLog.getPageNum()) && StringUtils.isNotNull(costLog.getPageSize())) {
            PageHelper.startPage(costLog.getPageNum(), costLog.getPageSize());
        }
        return costLogMapper.list(costLog);
    }

    @Override
    public CostLog item(Long id) {
        return costLogMapper.item(id);
    }

    @Override
    public CostLog newCostLog(Long id) {
        return costLogMapper.newCostLog(id);
    }

    @Override
    public int add(CostLog costLog) {
        if (StringUtils.isNull(costLog.getLawyerId())){
            return 0;
        }
        CostLog costLog1 = newCostLog(costLog.getLawyerId());
        Double sum = new Double(0);
        if (StringUtils.isNotNull(costLog1)&&StringUtils.isNotNull(costLog1.getSum())){
            sum = costLog1.getSum();
        }

        switch (costLog.getType()){
            case 0:
                //手动添加
                sum = sum + costLog.getCost();
                costLog.setRemark("手动添加");
                break;
            case 1:
                //手动减少
                sum = sum - costLog.getCost();
                costLog.setRemark("手动减少");
                break;
            case 2:
                //充值
                sum = sum + costLog.getCost();
                costLog.setRemark("充值");
                break;
            case 3:
                //消费
                sum = sum - costLog.getCost();
                costLog.setRemark("消费");
                break;
            default:
                return 0;
        }
        costLog.setSum(sum);
        return costLogMapper.add(costLog);
    }

    @Override
    public int edit(CostLog costLog) {
        return costLogMapper.edit(costLog);
    }

    @Override
    public int del(Long id) {
        return costLogMapper.del(id);
    }
}
