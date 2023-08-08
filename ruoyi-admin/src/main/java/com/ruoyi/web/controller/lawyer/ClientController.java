package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.service.laywer.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : ClientController
 * @Description : 客户
 * @Author : WANGKE
 * @Date: 2023-08-01 01:44
 */
@RestController
@RequestMapping("/client")
public class ClientController extends BaseController {
    @Autowired
    private ClientService clientService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:client:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Client client)
    {
        startPage();
        client.setPid(getUserId());
        List<Client> list = clientService.list(client);
        return getDataTable(list);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Client client)
    {
        return success(clientService.item(client.getId()));
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:client:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Client client)
    {
        client.setPhone(client.getPhone());
        List<Client> list = clientService.list(client);
        if (list.size()>0) {
            return error("当前用户已被绑定");
        }
        client.setCreateBy(getUsername());
        client.setPid(getUserId());
        client.setPname(getUsername());
        if (clientService.add(client) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:client:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Client client)
    {
        if (StringUtils.isNull(client)||StringUtils.isNull(client.getId())){
            return error("参数错误！");
        }
        client.setUpdateBy(getUsername());
        if (clientService.edit(client) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:client:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Client client)
    {
        if (StringUtils.isNull(client.getId())){
            return error("参数错误！");
        }
        if (clientService.del(client.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
