package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Goods;
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
        return success(goodsService.item(goods.getId()));
    }
}
