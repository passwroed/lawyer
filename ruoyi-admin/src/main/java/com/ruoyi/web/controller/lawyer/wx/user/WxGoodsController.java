package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Collectible;
import com.ruoyi.system.domain.lawyer.Goods;
import com.ruoyi.system.service.laywer.CollectibleService;
import com.ruoyi.system.service.laywer.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxGoodsController
 * @Description : 商品
 * @Author : WANGKE
 * @Date: 2023-08-10 15:43
 */
@RestController
@RequestMapping("/wxuser/goods")
public class WxGoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CollectibleService collectibleService;
    @Anonymous
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Goods goods)
    {
        startPage();
        List<Goods> list = goodsService.list(goods);
        return getDataTable(list);
    }
    @Anonymous
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Goods goods)
    {
        if (StringUtils.isNull(goods.getId())){
            return error("参数错误");
        }
        goods =goodsService.item(goods.getId());
                Collectible collectible = new Collectible();
        collectible.setClientId(getUserId());
        collectible.setGoodsId(goods.getId());
        if (StringUtils.isNotNull(collectibleService.item(collectible))){
            goods.setIsCollectible(1);
        }else {
            goods.setIsCollectible(0);
        }
        return success(goods);
    }
}
