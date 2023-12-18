package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.domain.lawyer.Task;
import com.ruoyi.system.domain.lawyer.TaskLog;
import com.ruoyi.system.service.laywer.LawyerService;
import com.ruoyi.system.service.laywer.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName : StatisticsController
 * @Description : 统计
 * @Author : WANGKE
 * @Date: 2023-09-09 23:09
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private LawyerService lawyerService;
    @PostMapping("/midLawyer")
    public AjaxResult midLawyer(@RequestBody Map<String,Object> map) {
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        for (SysRole role : listRole) {
            if (!role.getRoleKey().equals("admin") && !role.getRoleKey().equals("general")) {
                Lawyer lawyer = new Lawyer();
                lawyer.setUserId(getUserId());
                List<Lawyer> list = lawyerService.selectUserId(lawyer);
                if (list.size()>0){
                    lawyer = list.get(0);
                    map.put("lawyerId",lawyer.getId());
                }
            }
        }
        return success(statisticsService.midLawyer(map));
    }
    @PostMapping("/orderMoney")
    public AjaxResult orderMoney(@RequestBody Map<String,Object> map) {
        map.put("pid",getUserId());
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        for (SysRole role : listRole) {
            if (role.getRoleKey().equals("admin") || role.getRoleKey().equals("general")) {
                map.remove("pid");
            }
        }
        return success(statisticsService.orderMoney(map));
    }
    @PostMapping("/orderNum")
    public AjaxResult orderNum(@RequestBody Map<String,Object> map) {
        map.put("pid",getUserId());
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        for (SysRole role : listRole) {
            if (role.getRoleKey().equals("admin") || role.getRoleKey().equals("general")) {
                map.remove("pid");
            }
        }
        return success(statisticsService.orderNum(map));
    }
    @PostMapping("/typeTop")
    public AjaxResult typeTop(@RequestBody Map<String,Object> map) {
        List<SysRole> listRole = getLoginUser().getUser().getRoles();
        map.put("pid",getUserId());
        for (SysRole role : listRole) {
            if (role.getRoleKey().equals("admin") || role.getRoleKey().equals("general")) {
                map.remove("pid");
            }
        }
        return success(statisticsService.typeTop(map));
    }

    @PostMapping("/orderNumDay")
    public AjaxResult orderNumDay() {
        return success(statisticsService.orderNumDay());
    }
    @PostMapping("/userNumDay")
    public AjaxResult userNumDay() {
        return success(statisticsService.userNumDay());
    }
    @PostMapping("/orderAllNumDay")
    public AjaxResult orderAllNumDay() {
        return success(statisticsService.orderAllNumDay());
    }

}
