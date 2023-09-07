package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.file.QRCodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.service.laywer.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public TableDataInfo list(@RequestBody Client client) {
        startPage();
        client.setPid(getUserId());
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        for (SysRole role : listRole) {
            if (role.getRoleKey().equals("admin") || role.getRoleKey().equals("general")) {
                client.setPid(null);
            }
        }
        List<Client> list = clientService.list(client);
        return getDataTable(list);
    }

    @PostMapping("/item")
    public AjaxResult item(@RequestBody Client client) {
        return success(clientService.item(client.getId()));
    }

    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:client:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Client client) {
        client.setPhone(client.getPhone());
        List<Client> list = clientService.list(client);
        if (list.size() > 0) {
            return error("当前用户已被绑定");
        }
        client.setCreateBy(getUsername());
        client.setPid(getUserId());
        client.setPname(getUsername());
        if (clientService.add(client) == 0) {
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }

    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:client:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Client client) {
        if (StringUtils.isNull(client) || StringUtils.isNull(client.getId())) {
            return error("参数错误！");
        }
        client.setUpdateBy(getUsername());
        if (clientService.edit(client) == 0) {
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }

    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:client:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Client client) {
        if (StringUtils.isNull(client.getId())) {
            return error("参数错误！");
        }
        if (clientService.del(client.getId()) == 0) {
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }

    @PostMapping("/myQr")
    public AjaxResult myQr() {
        String filePath = RuoYiConfig.getUploadPath();
        File file = new File(filePath+"/"+String.valueOf(getUserId())+".png");
        Map<String,String> map = new HashMap<>();
        map.put("url","http://lawyer.admin.homelawyers.cn:12100/lawyer-admin/profile/upload/"+String.valueOf(getUserId())+".png");
        if (file.exists()) {
            System.out.println("已有文件");
            return success(map);
        }
        try {
            System.out.println("生成文件");
            QRCodeUtils.createCodeToFile("https://www.homelawyers.cn/pages/index/index?id="+String.valueOf(getUserId()), String.valueOf(getUserId()));

            return success(map);
        } catch (Exception e) {
            return success("获取二维码失败，请联系管理员");
        }
    }
}
