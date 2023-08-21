package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.TaskLog;

import java.util.List;

/**
 * @ClassName : TaskLogLogService
 * @Description : 任务日志
 * @Author : WANGKE
 * @Date: 2023-08-16 11:28
 */
public interface TaskLogService {
    public List<TaskLog> list(TaskLog taskLog);
    //新增
    public int add(TaskLog taskLog);
}
