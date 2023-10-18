package com.ruoyi.web.controller.lawyer.wx.user;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.WxUserAppConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.*;
import com.ruoyi.system.service.laywer.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName : WxOrderController
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-30 22:15
 */
@RestController
@RequestMapping("/wxuser/order")
public class WxOrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TaskService taskService;
    @Resource
    private WxUserAppConfig wxUserAppConfig;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Order order)
    {
        //获取商品
        Goods goods = goodsService.item(order.getGoodsId());
        if (StringUtils.isNull(goods)){
            return error("商品错误，请联系管理员");
        }else {
            order.setGoodsName(goods.getName());
            order.setMoney(goods.getMoney());
            order.setsImage(goods.getsImage());
            Goods goods1 = new Goods();
            goods1.setId(goods.getId());
            goods1.setNum(goods.getNum()+1);
            goodsService.edit(goods1);
        }
        //获取客户
        Client client = new Client();
        client.setUserId(getUserId());
        List<Client> list = clientService.list(client);
        if (list.size()==0) {
            return error("参数错误");
        }else {
            client = list.get(0);
            order.setClientId(client.getId());
            order.setClientName(order.getTask().getName());
            order.setClientPhone(order.getTask().getPhone());
            order.setPid(client.getPid());
            order.setpName(client.getPname());
        }

        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        Random r=new Random();
        order.setNo(sdf.format(System.currentTimeMillis())+r.nextInt(10));//规则：时间+1位随机数
        System.out.println("order no ==============="+order.getNo());
        order.setCreateBy(getUsername());
        order.setStatus(0);
        order.setType(0);
        //添加任务
        Task task = order.getTask();
        if (StringUtils.isNull(order.getTask())){
            task = new Task();
        }
        task.setOrderNo(order.getNo());
        task.setName(goods.getName());
        task.setPhone(order.getTask().getPhone());
        task.setcName(order.getClientName());
        task.setMoney(goods.getMoney());
        task.setProfit(order.getTask().getProfit());
        task.setStatus(-1);
        if (taskService.add(task) == 0){
            return error("下单失败，请联系管理员！");
        }
        order.setTaskNo(task.getNo());
        Map payMap = orderService.add(order,wxUserAppConfig.getAppId());
        if (StringUtils.isNull(payMap)){
            return error("下单失败，请联系管理员！");
        }
        System.out.println("发送成功="+JSONObject.toJSONString(order));
        rocketMQTemplate.syncSend("test-topic-delay", MessageBuilder.withPayload(JSONObject.toJSONString(order)).build(),3000,9);
        return success(payMap);
    }
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Order order)
    {
        Client client = new Client();
        client.setUserId(getUserId());
        List<Client> listClient = clientService.list(client);
        if (listClient.size()==0) {
            return errorDataTable("参数错误");
        }else {
            client = listClient.get(0);
        }
        startPage();
        order.setClientId(client.getId());
        List<Order> list = orderService.list(order);
//        List<Order> returnList = new ArrayList<>();
//        for (Order order1:list) {
//            if (StringUtils.isNotNull(order1.getTaskNo())){
//                Task task = taskService.itemNo(order1.getTaskNo());
//                order1.setTask(task);
//            }
//            returnList.add(order1);
//        }
        return getDataTable(list);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Order order)
    {
        Map<String,Object> map = new HashMap<>();
        order = orderService.item(order.getId());
        if (StringUtils.isNull(order)){
            return success();
        }
        map.put("order",order);
        //获取任务
        Task task = taskService.itemNo(order.getTaskNo());
        if (StringUtils.isNotNull(task)){
            map.put("task",task);
        }
        //获取客户
        Client client = clientService.item(order.getClientId());
        if (StringUtils.isNotNull(client)){
            map.put("client",client);
        }
        //如果状态==5显示理由
        if (StringUtils.isNotNull(order)||StringUtils.isNotNull(order.getStatus())){
            if (order.getStatus() == 5||order.getStatus() == -2||order.getStatus() == 6||order.getStatus() == 7){
                OrderLog orderLog = orderLogService.itemOrderId(order.getId());
                if (StringUtils.isNotNull(orderLog)){
                    map.put("orderLog",orderLog);
                }

            }
        }
        return success(map);
    }
    @PostMapping("/getPayItem")
    public AjaxResult getPayItem(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getId())){
            return error("参数错误");
        }
        order = orderService.item(order.getId());
        System.out.println(JSON.toJSONString(order));
        if (StringUtils.isNotNull(order)&&StringUtils.isNotNull(order.getStatus())&&order.getStatus() == 0){
            return success(orderService.payWxMap(order,wxUserAppConfig.getAppId()));
        }else {
            return error("非未支付订单");
        }
    }
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getId())){
            return error("参数错误！");
        }
        order = orderService.item(order.getId());

        if (StringUtils.isNotNull(order)&&StringUtils.isNotNull(order.getStatus()) && order.getStatus() == 0){
            order.setStatus(-3);
            orderService.edit(order);
            return success("操作成功");
        }else {
            return error("此订单不能取消");
        }


    }
    //删除订单
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getId())){
            return error("参数错误！");
        }
        order = orderService.item(order.getId());
        if (order.getStatus()==-3||order.getStatus()==-1||order.getStatus()==-0||order.getStatus()==4||order.getStatus()==7){
            if (orderService.del(order.getId()) == 0){
                return error("删除失败，请联系管理员！");
            }
        }else {
            return error("订单进行中，暂不能删除订单");
        }

        return success("操作成功");
    }
    //申请退款
    @PostMapping("/application")
    public AjaxResult application(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getId())||StringUtils.isNull(order.getOrderLog())){
            return error("参数错误！");
        }
        Order orderOld = orderService.item(order.getId());
        Client client = clientService.itemUserId(getUserId());
        if (StringUtils.isNull(orderOld) || StringUtils.isNull(client)){
            return error("参数错误！");
        }
        if (orderOld.getClientId() != client.getId()){
            return error("非本人不可操作！");
        }
        if (orderOld.getStatus()==5){
            return error("已申请退款，请等待审核！");
        }
        Order order1 = new Order();
        order1.setId(order.getId());
        order1.setStatus(5);
        if (orderService.edit(order1)==0){
            return error("申请退款失败，请联系管理员！");
        }
        OrderLog orderLog = order.getOrderLog();
        orderLog.setStatus(5);
        orderLog.setOrderId(order.getId());
        orderLogService.add(orderLog);
        return success("操作成功");
    }
}
