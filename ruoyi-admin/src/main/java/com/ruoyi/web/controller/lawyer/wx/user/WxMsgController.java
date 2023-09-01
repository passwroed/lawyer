package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.domain.lawyer.Msg;
import com.ruoyi.system.service.laywer.ClientService;
import com.ruoyi.system.service.laywer.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : MsgController
 * @Description : 站内消息
 * @Author : WANGKE
 * @Date: 2023-09-01 11:38
 */
@RestController
@RequestMapping("/wxuser/msg")
public class WxMsgController extends BaseController {
    @Autowired
    private MsgService msgService;
    @Autowired
    private ClientService clientService;
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Msg msg)
    {
        startPage();
        Client client = new Client();
        client.setUserId(getUserId());
        List<Client> listClient = clientService.list(client);
        if (listClient.size()==0){
            return errorDataTable("未登录");
        }
        client = listClient.get(0);
        msg.setClientId(client.getId());
        List<Msg> list = msgService.list(msg);
        return getDataTable(list);
    }
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Msg msg)
    {
        msg.setStatus(1);
        if (msgService.edit(msg)==0){
            return error("操作失败");
        }
        return success();
    }
    @PostMapping("/testadd")
    public AjaxResult testadd(){
        Msg msg = new Msg();
        msg.setClientId(2l);
        msg.setType(1);
        msg.setStatus(0);
        msg.setMsg("尊敬的用户您好：您的编号为："+1234+"的订单，"+"已购买成功，"+"您可以前往我的->我的订单中查看详情进度");
        msgService.add(msg);
        return success();
    }
}
