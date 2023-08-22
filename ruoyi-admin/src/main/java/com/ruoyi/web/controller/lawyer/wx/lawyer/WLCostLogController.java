package com.ruoyi.web.controller.lawyer.wx.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.service.laywer.CostLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WLCostLogController
 * @Description : 消费记录
 * @Author : WANGKE
 * @Date: 2023-08-22 18:33
 */
@RestController
@RequestMapping("/wxLawyer/costLog")
public class WLCostLogController extends BaseController {
    @Autowired
    private CostLogService costLogService;

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody CostLog costLog) {
        costLog.setLawyerId(getLawyerId());
        return getDataTable(costLogService.list(costLog));
    }
}
