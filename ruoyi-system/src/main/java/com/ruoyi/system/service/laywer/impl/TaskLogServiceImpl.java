package com.ruoyi.system.service.laywer.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.TaskLog;
import com.ruoyi.system.mapper.lawyer.TaskLogMapper;
import com.ruoyi.system.service.laywer.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : TaskLogServiceImpl
 * @Description : 任务日志
 * @Author : WANGKE
 * @Date: 2023-08-16 11:33
 */
@Service
public class TaskLogServiceImpl implements TaskLogService {
    @Autowired
    private TaskLogMapper taskLogMapper;
    @Override
    public List<TaskLog> list(TaskLog taskLog) {
        if (StringUtils.isNotNull(taskLog.getPageNum()) && StringUtils.isNotNull(taskLog.getPageSize())) {
            PageHelper.startPage(taskLog.getPageNum(), taskLog.getPageSize());
        }else {
            PageHelper.startPage(1, 999);
        }
        return taskLogMapper.list(taskLog);
    }

    @Override
    public int add(TaskLog taskLog) {
        return taskLogMapper.add(taskLog);
    }
}
