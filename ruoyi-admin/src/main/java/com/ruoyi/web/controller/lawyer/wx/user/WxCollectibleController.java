package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Collectible;
import com.ruoyi.system.domain.lawyer.Goods;
import com.ruoyi.system.domain.lawyer.Order;
import com.ruoyi.system.service.laywer.CollectibleService;
import com.ruoyi.system.service.laywer.GoodsService;
import com.ruoyi.system.service.laywer.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : CollectibleController
 * @Description : 收藏
 * @Author : WANGKE
 * @Date: 2023-09-01 03:10
 */
@RestController
@RequestMapping("/wxuser/collectible")
public class WxCollectibleController extends BaseController {
    @Autowired
    private CollectibleService collectibleService;
    @Autowired
    private GoodsService goodsService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:collectible:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Collectible collectible)
    {
        startPage();
        collectible.setClientId(getUserId());
        List<Collectible> list = collectibleService.list(collectible);
        return getDataTable(list);
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:collectible:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Collectible collectible)
    {
        Goods goods = goodsService.item(collectible.getGoodsId());
        collectible.setGoodsName(goods.getName());
        collectible.setMoney(goods.getMoney());
        collectible.setsImage(goods.getsImage());
        collectible.setClientId(getUserId());
        collectible.setCreateBy(getUsername());
        if (collectibleService.add(collectible) == 0){
            return error("收藏失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:collectible:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Collectible collectible)
    {
        if (StringUtils.isNull(collectible.getId())){
            return error("参数错误！");
        }
        if (collectibleService.del(collectible.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
