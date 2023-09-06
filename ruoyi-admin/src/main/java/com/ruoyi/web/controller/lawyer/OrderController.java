package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.config.WxUserAppConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.*;
import com.ruoyi.system.service.laywer.*;
import org.apache.poi.ss.formula.ptg.Area2DPtgBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName : OrderController
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-02 14:30
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Resource
    private WxUserAppConfig wxUserAppConfig;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private OrderLogService orderLogService;
    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:order:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Order order)
    {
        startPage();
        order.setPid(getUserId());
        List<Order> list = orderService.list(order);
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
        Client client = clientService.itemUserId(order.getClientId());
        if (StringUtils.isNotNull(client)){
            map.put("client",client);
        }
        //如果状态==5显示理由
        if (StringUtils.isNotNull(order)||StringUtils.isNotNull(order.getStatus())){
            if (order.getStatus() == 5||order.getStatus() == -2||order.getStatus() == 6||order.getStatus() == 7){
                OrderLog orderLog = new OrderLog();
                orderLog.setOrderId(order.getId());
                orderLogService.list(orderLog);
            }
        }
        return success(map);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:order:add')")
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
        }
        //获取客户
        if (StringUtils.isNotNull(order.getClientId())){
            Client client = clientService.item(order.getClientId());
            if (StringUtils.isNull(client)){
                return error("用户错误，请联系管理员");
            }else {
                order.setClientName(client.getName());
                order.setPid(client.getPid());
                order.setpName(client.getPname());
            }
        }else {
            Client client = new Client();
            client.setPhone(order.getTask().getPhone());
            List<Client> list = clientService.list(client);
            if (list.size()>0) {
                return error("当前用户已被绑定");
            }else {

            }
            client.setName(order.getTask().getcName());
            client.setCreateBy(getUsername());
            client.setPid(1L);
            client.setPname("admin");
            client.setAreaCode(Math.toIntExact(order.getTask().getPovinceId()));
            if (client.getAreaCode()!=null && client.getAreaCode()>0){
                String name = "";
                Area area = new Area();
                area = areaService.iDArea(Long.valueOf(client.getAreaCode()));
                name = area.getName();
                while (area.getPid() > 0){
                    area = areaService.pArea(Long.valueOf(area.getPid()));
                    name = area.getName()+"-"+name;
                }
                client.setArea(name);
            }
            if (clientService.add(client) == 0){
                return error("下单失败，请联系管理员！");
            }
            order.setClientId(client.getId());
            order.setClientName(client.getName());
            order.setPid(client.getPid());
            order.setpName(client.getPname());
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        Random r=new Random();
        order.setNo(sdf.format(System.currentTimeMillis())+r.nextInt(10));//规则：时间+1位随机数
        //添加任务
        Task task = order.getTask();
        task.setOrderNo(order.getNo());
        task.setName(goods.getName());
        if (taskService.add(task) == 0){
            return error("下单失败，请联系管理员！");
        }
        order.setTaskNo(task.getNo());
        order.setCreateBy(getUsername());
        order.setStatus(0);
        Map payMap = orderService.add(order,wxUserAppConfig.getAppId());
        if (StringUtils.isNull(payMap)){
            return error("下单失败，请联系管理员！");
        }
        return success(payMap);
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:order:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Order order)
    {
        if (StringUtils.isNull(order)||StringUtils.isNull(order.getId())){
            return error("参数错误！");
        }
        order.setUpdateBy(getUsername());
        if (orderService.edit(order) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:order:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getId())){
            return error("参数错误！");
        }
        if (orderService.del(order.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
    @PostMapping("/application")
    public AjaxResult application(@RequestBody Order order)
    {
        if (StringUtils.isNull(order.getId())||StringUtils.isNull(order.getStatus())){
            return error("参数错误！");
        }
        Order order1 = orderService.item(order.getId());
        if (StringUtils.isNull(order1)){
            return error("未找到该订单");
        }
        if (order1.getStatus() != 5){
            return error("该订单，不可申请退款");
        }
        OrderLog orderLog = order.getOrderLog();
        if (StringUtils.isNull(orderLog)){
            orderLog = new OrderLog();
        }
        orderLog.setStatus(order.getStatus());
        orderLog.setOrderId(order.getId());
        orderLogService.add(orderLog);
        if (order.getStatus()==6){
            //同意退款
            orderService.refund(order);
        }
        if (orderService.edit(order)==0){
            return error("失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
