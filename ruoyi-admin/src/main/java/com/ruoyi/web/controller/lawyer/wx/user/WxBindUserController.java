package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Banner;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.laywer.BannerService;
import com.ruoyi.system.service.laywer.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : BindUserController
 * @Description : 绑定用户
 * @Author : WANGKE
 * @Date: 2023-09-09 22:40
 */
@RestController
@RequestMapping("/wxuser/bindUser")
public class WxBindUserController extends BaseController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ISysUserService iSysUserService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:banner:list')")
    @Anonymous
    @PostMapping("/bind")
    public AjaxResult list(@RequestBody SysUser sysUser) {
        if (StringUtils.isNull(sysUser.getUserId())){
            return error("参数失败");
        }
        Client client = clientService.itemUserId(getUserId());
        if (StringUtils.isNull(client)){
            return error("请登录");
        }
        SysUser sysUser1 = iSysUserService.selectUserById(sysUser.getUserId());
        if (StringUtils.isNull(sysUser1)){
            return error("未找到客服");
        }
        Client client1 = new Client();
        client1.setId(client.getId());
        client.setPid(sysUser1.getUserId());
        client.setPname(sysUser1.getNickName());
        if (clientService.edit(client1) == 0){
            return error("绑定失败");
        }
        return success("操作成功");
    }
}
