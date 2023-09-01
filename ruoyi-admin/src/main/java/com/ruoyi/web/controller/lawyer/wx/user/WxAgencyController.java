package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.lawyer.Agency;
import com.ruoyi.system.service.laywer.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : WxAgencyController
 * @Description : 代理
 * @Author : WANGKE
 * @Date: 2023-08-10 14:54
 */
@RestController
@RequestMapping("/wxuser/angency")
public class WxAgencyController extends BaseController {
    @Autowired
    private AgencyService agencyService;
    @Anonymous
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Agency agency)
    {
        agency.setCreateBy(getUsername());
        if (agencyService.add(agency) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
