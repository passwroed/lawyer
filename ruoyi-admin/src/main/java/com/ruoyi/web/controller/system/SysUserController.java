package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.domain.lawyer.CostLog;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.service.laywer.AreaService;
import com.ruoyi.system.service.laywer.ClientService;
import com.ruoyi.system.service.laywer.CostLogService;
import com.ruoyi.system.service.laywer.LawyerService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;
    @Autowired
    private LawyerService lawyerService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private CostLogService costLogService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }
    @GetMapping("/wxList")
    public TableDataInfo wxList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectWxUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            SysUser user1=userService.checkPhoneUserType(user);
            if (StringUtils.isNotNull(user1)){
                user.setUserId(user1.getUserId());
                user.setUserType("00");
                return toAjax(userService.updateUser(user));
            }else {
                return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
            }
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            SysUser user1=userService.checkPhoneUserType(user);
            if (StringUtils.isNotNull(user1)){
                user.setUserId(user1.getUserId());
                user.setUserType("00");
                return toAjax(userService.updateUser(user));
            }else {
                return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
            }

        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    public AjaxResult deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody SysUser user) {
        user.setUserId(getUserId());
        if (userService.updateUser(user) == 0) {
            return error("操作失败，请联系管理员");
        }
        Client client = clientService.itemUserId(getUserId());
        if (StringUtils.isNotNull(client)) {
            if (StringUtils.isNotNull(user.getNickName())){
                if (StringUtils.isNotNull(user.getAreaCode())) {
                    client.setAreaCode(user.getAreaCode());
                    String name = "";
                    Area area = areaService.iDArea(Long.valueOf(client.getAreaCode()));
                    name = area.getName();
                    while (area.getPid() > 0) {
                        area = areaService.pArea(Long.valueOf(area.getPid()));
                        name = area.getName() + "-" + name;
                    }
                    client.setArea(name);
                }
                client.setName(user.getNickName());
                client.setNickName(user.getNickName());
                clientService.edit(client);
            }
        }
        if (StringUtils.isNotNull(getLawyerId())) {
            Lawyer lawyer = new Lawyer();
            if (StringUtils.isNotNull(user.getAreaCode())) {
                lawyer.setAreaCode(user.getAreaCode());
                String name = "";
                Area area = areaService.iDArea(Long.valueOf(lawyer.getAreaCode()));
                name = area.getName();
                while (area.getPid() > 0) {
                    area = areaService.pArea(Long.valueOf(area.getPid()));
                    name = area.getName() + "-" + name;
                }
                lawyer.setArea(name);
            }
            if (StringUtils.isNotNull(user.getLicenseNum())) {
                lawyer.setLicenseNum(user.getLicenseNum());
            }

            lawyer.setUserId(getLoginUser().getUserId());
            List<Lawyer> list = lawyerService.selectUserId(lawyer);
            if (list.size() > 0) {
                lawyer = list.get(0);
                lawyerService.edit(lawyer);
            }
        }

        return success();
    }

    @PostMapping("/item")
    public AjaxResult item() {
        SysUser sysUser = userService.selectUserById(getLoginUser().getUserId());
        sysUser.setPhonenumber(sysUser.getPhonenumber().replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2"));
        Lawyer lawyer = new Lawyer();
        lawyer.setUserId(getLoginUser().getUserId());
        System.out.println(JSON.toJSONString(sysUser));
        Map map = JSON.parseObject(JSON.toJSONString(sysUser), Map.class);

        System.out.println(JSON.toJSONString(lawyer));
        List<Lawyer> list = lawyerService.selectUserId(lawyer);
        System.out.println(JSON.toJSONString(list));
        if (list.size() > 0) {
            lawyer = list.get(0);
            lawyer.setPhone(sysUser.getPhonenumber().replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1****$2"));
            map.put("lawyer", JSON.parseObject(JSON.toJSONString(lawyer), Map.class));
            System.out.println("id ---------------------->" + lawyer.getId());
            CostLog costLog = costLogService.newCostLog(lawyer.getId());
            if (StringUtils.isNotNull(costLog)) {
                System.out.println("item ---------------------->" + costLog.getSum());
                map.put("cost", costLog.getSum());
            }
        }
        return success(map);
    }
}
