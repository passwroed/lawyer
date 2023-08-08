package com.ruoyi.system.service.laywer;

import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.domain.lawyer.Help;

import java.util.List;

/**
 * @ClassName : ClientService
 * @Description : 客户
 * @Author : WANGKE
 * @Date: 2023-08-01 01:08
 */
public interface ClientService {
    //列表
    public List<Client> list(Client client);
    //详情
    public Client item(Long id);
    //新增
    public int add(Client client);
    //手动新增
    public int headAdd(Client client);
    //编辑
    public int edit(Client client);
    //删除
    public int del(Long id);
}
