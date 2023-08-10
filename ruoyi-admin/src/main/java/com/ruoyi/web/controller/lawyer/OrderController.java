package com.ruoyi.web.controller.lawyer;

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

import java.util.List;

/**
 * @ClassName : OrderController
 * @Description : 订单
 * @Author : WANGKE
 * @Date: 2023-08-02 14:30
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

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
        return success(orderService.item(order.getId()));
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

        //添加任务
        Task task = order.getTask();
        task.setName(goods.getName());
        if (taskService.add(task) == 0){
            return error("下单失败，请联系管理员！");
        }
        order.setTaskNo(task.getNo());
        order.setCreateBy(getUsername());
        order.setStatus(0);
        if (orderService.add(order) == 0){
            return error("下单失败，请联系管理员！");
        }
        return success("操作成功");
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

}