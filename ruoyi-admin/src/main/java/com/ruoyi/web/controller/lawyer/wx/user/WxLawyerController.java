package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.service.laywer.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxLawyerLawyerController
 * @Description : 用户端律师
 * @Author : WANGKE
 * @Date: 2023-08-09 16:14
 */
@RestController
@RequestMapping("/wxuser/lawyer")
public class WxLawyerController extends BaseController {
    @Autowired
    private LawyerService lawyerService;

    @Anonymous
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Lawyer lawyer)
    {
        startPage();
        List<Lawyer> list = lawyerService.list(lawyer);
        return getDataTable(list);
    }
    @Anonymous
    @PostMapping("/headImageList")
    public TableDataInfo headImageList(@RequestBody Lawyer lawyer)
    {
        startPage();
        List<Lawyer> list = lawyerService.headImageList(lawyer);
        return getDataTable(list);
    }
    @Anonymous
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Lawyer lawyer)
    {
        if (StringUtils.isNull(lawyer)||StringUtils.isNull(lawyer.getId())){
            return error("参数错误！");
        }
        return success(lawyerService.item(lawyer.getId()));
    }
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Lawyer lawyer)
    {
        lawyer.setCreateBy(getUsername());
        lawyer.setType(1);
        switch (lawyerService.add(lawyer)){
            case -1:
                return error("当前手机号已绑定！");
            case 0:
                return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
