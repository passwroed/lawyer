package com.ruoyi.web.controller.lawyer.wx.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.service.laywer.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxLawyerLawyerController
 * @Description : WX律师
 * @Author : WANGKE
 * @Date: 2023-08-22 17:00
 */
@RestController
@RequestMapping("/wxLawyer/lawyer")
public class WLLawyerController extends BaseController {

    @Autowired
    private LawyerService lawyerService;
    @PostMapping("/status")
    public AjaxResult status(@RequestBody Lawyer lawyer)
    {
        if (StringUtils.isNull(lawyer)
                ||StringUtils.isNull(lawyer.getStatus())
        ||lawyer.getStatus()<2){
            return error("参数错误！");
        }
        Lawyer olawyer = lawyerService.item(getLawyerId());
        if (olawyer.getStatus()<1){
            return error("无权限编辑");
        }
        Lawyer lawyer1 = new Lawyer();
        lawyer1.setId(getLawyerId());
        lawyer1.setStatus(lawyer.getStatus());
        lawyer1.setUpdateBy(getUsername());
        if (lawyerService.status(lawyer1) == 0){
            return error("状态变更失败，请联系管理员！");
        }
        return success("操作成功");
    }
    @PostMapping("/typeList")
    public TableDataInfo typeList(@RequestBody Lawyer lawyer)
    {
        startPage();
        List<Lawyer> list = lawyerService.typeList(lawyer);
        return getDataTable(list);
    }
}
