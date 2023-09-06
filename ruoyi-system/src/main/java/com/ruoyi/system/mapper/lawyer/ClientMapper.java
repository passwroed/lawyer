package com.ruoyi.system.mapper.lawyer;

import com.ruoyi.system.domain.lawyer.Client;

import java.util.List;

/**
 * @ClassName : ClientMapper
 * @Description : 客户
 * @Author : WANGKE
 * @Date: 2023-08-01 01:06
 */
public interface ClientMapper {
    //列表
    public List<Client> list(Client client);
    //详情
    public Client item(Long id);
    public Client itemUserId(Long userId);
    //新增
    public int add(Client client);
    //手动新增
    public int headAdd(Client client);
    //编辑
    public int edit(Client client);
    //删除
    public int del(Long id);
}
