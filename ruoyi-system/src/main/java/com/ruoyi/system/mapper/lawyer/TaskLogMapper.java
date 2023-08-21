package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.TaskLog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : TaskLogMapper
 * @Description : 任务日志
 * @Author : WANGKE
 * @Date: 2023-08-16 11:14
 */
public interface TaskLogMapper {
    public List<TaskLog> list(TaskLog taskLog);
    //新增
    public int add(TaskLog taskLog);
    //编辑
    public int edit(TaskLog taskLog);
    //删除
    public int del(Long id);
}
