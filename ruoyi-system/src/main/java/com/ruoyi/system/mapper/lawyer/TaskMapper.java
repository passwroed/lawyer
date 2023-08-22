package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Task;

import java.util.List;

/**
 * @ClassName : TaskMapper
 * @Description : 任务
 * @Author : WANGKE
 * @Date: 2023-08-02 14:45
 */
public interface TaskMapper {
    //列表
    public List<Task> list(Task task);
    public List<Task> lawyer1list(Task task);
    //详情
    public Task item(Long id);
    public Task itemNo(String taskNo);
    //新增
    public int add(Task task);
    //编辑
    public int edit(Task task);
    //删除
    public int del(Long id);
    public int reject(Long id);
}
