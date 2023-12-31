package com.ruoyi.framework.web.service;

import javax.annotation.Resource;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.lawyer.Client;
import com.ruoyi.system.domain.lawyer.Lawyer;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.lawyer.ClientMapper;
import com.ruoyi.system.service.laywer.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.BlackListException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

import java.util.List;
import java.util.Random;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private LawyerService lawyerService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        loginUser.setLawyerId(13L);
        loginUser.setLawyerName("名称");
        loginUser.setLawyerType(0);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }
    /**
     * 用户微信登录
     *
     * @param openId 登录凭据 只能用一次
     * @return
     */
    public String wxUserLogin(String openId,String phone){
        //还可以获取其他信息
        //依据openid判别数据库中是否有该用户
        //依据openid查询用户信息
        SysUser wxUser = userMapper.selectWxUserByPhone(phone);
        //假如查不到，则新增，查到了，则更新
        SysUser user = new SysUser();
        if (wxUser == null) {
            // 新增
            user.setUserName(getStringRandom(16));// 生成16位随机用户名
            user.setNickName("微信用户");// 生成16位随机用户名
            user.setOpenId(openId);
            user.setUserType("01");
            user.setPhonenumber(phone);
            //新增 用户
            userMapper.insertUser(user);

        }else {
            //更新
            user = wxUser;
            user.setOpenId(openId);
            user.setPhonenumber(phone);
            userMapper.updateUser(user);
        }
        //组装token信息
        LoginUser loginUser = new LoginUser();
        loginUser.setOpenId(openId);
        loginUser.setOpenId(user.getOpenId());
        //假如有的话设置
        loginUser.setUser(user);
        loginUser.setUserId(user.getUserId());
        Client client = new Client();
        client.setPhone(phone);
        List<Client> list = clientMapper.list(client);
        if (StringUtils.isNull(list)||list.size()==0){
            client.setName("微信用户");
            client.setNickName("微信用户");
            client.setPid(1L);
            client.setPname("admin");
            client.setUserId(user.getUserId());
            clientMapper.add(client);
        }else {
            if (StringUtils.isNull(list.get(0).getUserId())){
                client = list.get(0);
                client.setUserId(wxUser.getUserId());
            }
        }
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 律师微信登录
     *
     * @param openId 登录凭据 只能用一次
     * @return
     */
    public String wxLawyerLogin(String unionid,String openId,String phone){
        //还可以获取其他信息
        //依据openid判别数据库中是否有该用户
        //依据openid查询用户信息
        SysUser wxUser = userMapper.selectWxUserByPhone(phone);
        //假如查不到，则新增，查到了，则更新
        SysUser user = new SysUser();
        System.out.println("手机号"+phone);
        if (wxUser == null) {
            // 新增
            user.setUserName(getStringRandom(16));// 生成16位随机用户名
            user.setOpenId(openId);
            user.setUserType("02");
            user.setNickName("微信律师用户");// 生成16位随机用户名
            user.setPhonenumber(phone);
            user.setUnionId(unionid);
            userMapper.insertUser(user);
        }else {
            //更新
            user = wxUser;
            user.setPhonenumber(phone);
            user.setOpenId(openId);
            user.setUnionId(unionid);
            userMapper.updateUser(user);
        }
        //组装token信息
        LoginUser loginUser = new LoginUser();
        loginUser.setOpenId(openId);
        //假如有的话设置
        loginUser.setUser(user);
        loginUser.setUserId(user.getUserId());
        loginUser.setOpenId(user.getOpenId());
        Lawyer lawyer = new Lawyer();
        lawyer.setUserId(user.getUserId());
        List<Lawyer> lawyerList = lawyerService.selectUserId(lawyer);
        if (lawyerList.size()>0&&StringUtils.isNotNull(lawyerList.get(0).getId())){
            loginUser.setLawyerId(lawyerList.get(0).getId());
            loginUser.setLawyerType(lawyerList.get(0).getType());
            loginUser.setLawyerName(lawyerList.get(0).getName());
        }else {
            System.out.println("未找到律师");
            Lawyer lawyer1 = new Lawyer();
            lawyer1.setPhone(phone);
            List<Lawyer> lawyerList1= lawyerService.list(lawyer1);
            System.out.println("未找到律师，律师数:"+lawyerList1.size());
            if (lawyerList1.size()>0){
                System.out.println("未找到律师，律师id:"+lawyerList1.get(0).getId());
                lawyer1 = lawyerList1.get(0);
                lawyer1.setUserId(user.getUserId());
                lawyerService.edit(lawyer1);
                loginUser.setLawyerId(lawyerList1.get(0).getId());
                loginUser.setLawyerType(lawyerList1.get(0).getType());
                loginUser.setLawyerName(lawyerList1.get(0).getName());
            }
        }
        // 生成token
        return tokenService.createToken(loginUser);
    }
    //生成随机用户名，数字和字母组成,
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表明生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母仍是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母仍是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
