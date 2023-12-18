package com.ruoyi.web.controller.system;

import java.security.spec.AlgorithmParameterSpec;
import java.util.*;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.WxLawyerAppConfig;
import com.ruoyi.common.config.WxUserAppConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.WxLoginBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysMenuService;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;
import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private WxLawyerAppConfig wxLawyerAppConfig;

    @Autowired
    private WxUserAppConfig wxUserAppConfig;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
    @Anonymous
    @PostMapping("/wxLawyerLogin")
    public AjaxResult wxLawyerLogin(@RequestBody WxLoginBody wxLoginBody) {
        String loginCode = wxLoginBody.getLoginCode();
        String phoneCode = wxLoginBody.getPhoneCode();
        //WXContent.APPID是自定义的全局变量
        String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", wxLawyerAppConfig.getAppId(), wxLawyerAppConfig.getAppSecret());
        String tokenRes = restTemplate.getForObject(tokenUrl, String.class);
        JSONObject tokenObject = JSONObject.parseObject(tokenRes);
        //获取手机号
        String phoneUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + tokenObject.getString("access_token");
        //封装请求体
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("code", phoneCode);

        //封装请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramMap,headers);
        JSONObject phoneObject = JSONObject.parseObject(restTemplate.postForEntity(phoneUrl, httpEntity,String.class).getBody());
        System.out.println(phoneObject.toString());
        String phone = "";
        if (phoneObject.getInteger("errcode") != 0) {

            return AjaxResult.error("微信登录失败！");
        } else {
            phone = phoneObject.getJSONObject("phone_info").getString("phoneNumber");
        }

        //向微信服务器发送恳求获取用户信息
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxLawyerAppConfig.getAppId() + "&secret=" + wxLawyerAppConfig.getAppSecret() + "&js_code=" + loginCode + "&grant_type=authorization_code";
        String res = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(res);
        System.out.println("开始openid");
        System.out.println(jsonObject.toString());
        //获取session_key和openid
        String sessionKey = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");
        String unionid = jsonObject.getString("unionid");
        if (StringUtils.isNull(jsonObject.getInteger("errcode"))){
            //假如解析成功,获取token
            String token = loginService.wxLawyerLogin(unionid,openid,phone);
            if (StringUtils.isNull(token)){
                return AjaxResult.error("您尚未注册成为律师，请先完成注册");
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, token);
            return ajax;
        } else {
            return AjaxResult.error("微信登录失败！");
        }
    }

    @Anonymous
    @PostMapping("/wxUserLogin")
    public AjaxResult wxUserLogin(@RequestBody WxLoginBody wxLoginBody) {
        String loginCode = wxLoginBody.getLoginCode();
        String phoneCode = wxLoginBody.getPhoneCode();
        //WXContent.APPID是自定义的全局变量
        String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", wxUserAppConfig.getAppId(), wxUserAppConfig.getAppSecret());
        String tokenRes = restTemplate.getForObject(tokenUrl, String.class);
        JSONObject tokenObject = JSONObject.parseObject(tokenRes);
        //获取手机号
        String phoneUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + tokenObject.getString("access_token");
        //封装请求体
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("code", phoneCode);

        //封装请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramMap,headers);
        JSONObject phoneObject = JSONObject.parseObject(restTemplate.postForEntity(phoneUrl, httpEntity,String.class).getBody());

        String phone = "";
        if (phoneObject.getInteger("errcode") != 0) {
            return AjaxResult.error("微信登录失败！");
        } else {
            phone = phoneObject.getJSONObject("phone_info").getString("phoneNumber");
        }

        //向微信服务器发送恳求获取用户信息
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxUserAppConfig.getAppId() + "&secret=" + wxUserAppConfig.getAppSecret() + "&js_code=" + loginCode + "&grant_type=authorization_code";
        String res = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(res);
        //获取session_key和openid
        String sessionKey = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");
        System.out.println("开始openid");
        System.out.println(jsonObject.toString());
        if (StringUtils.isNotNull(openid)){
            //假如解析成功,获取token
            String token = loginService.wxUserLogin(openid, phone);
            if (StringUtils.isNull(token)){
                return AjaxResult.error("您尚未注册，请先完成注册");
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, token);
            return ajax;
        } else {
            return AjaxResult.error("微信登录失败！");
        }
    }

    /**
     * AES解密
     */
    private String decrypt(String sessionKey, String encryptedIv, String encryptedData) throws Exception {
        // 转化为字节数组
        byte[] key = Base64.decode(sessionKey);
        byte[] iv = Base64.decode(encryptedIv);
        byte[] encData = Base64.decode(encryptedData);
        // 假如密钥缺乏16位，那么就补足
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        // 假如初始向量缺乏16位，也补足
        if (iv.length % base != 0) {
            int groups = iv.length / base + (iv.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(iv, 0, temp, 0, iv.length);
            iv = temp;
        }
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        String resultStr = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            resultStr = new String(cipher.doFinal(encData), "UTF-8");
        } catch (Exception e) {
            //            logger.info("解析错误");
            e.printStackTrace();
        }
        // 解析加密后的字符串
        return resultStr;
    }

}
