package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Task;

import java.util.List;

/**
 * @ClassName : TaskService
 * @Description : 任务
 * @Author : WANGKE
 * @Date: 2023-08-02 15:07
 */
public interface TaskService {
    public List<Task> list(Task task);
    public List<Task> lawyer1list(Task task);
    //详情
    public Task item(Long id);
    public Task itemNo(String no);
    //新增
    public int add(Task task);
    //编辑
    public int edit(Task task);
    //删除
    public int del(Long id);
    public int reject(Long id);
}
