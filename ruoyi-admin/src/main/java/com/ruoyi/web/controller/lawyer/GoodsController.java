package com.ruoyi.web.controller.lawyer;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.Goods;
import com.ruoyi.system.service.laywer.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : GoodsController
 * @Description : 客户
 * @Author : WANGKE
 * @Date: 2023-08-01 01:44
 */
@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {
    @Autowired
    private GoodsService goodsService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:goods:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Goods goods)
    {
        startPage();
        List<Goods> list = goodsService.list(goods);
        return getDataTable(list);
    }
    @PostMapping("/item")
    public AjaxResult item(@RequestBody Goods goods)
    {
        return success(goodsService.item(goods.getId()));
    }
    //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:goods:add')")
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody Goods goods)
    {
        goods.setCreateBy(getUsername());
        if (goodsService.add(goods) == 0){
            return error("新增失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:goods:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody Goods goods)
    {
        if (StringUtils.isNull(goods)||StringUtils.isNull(goods.getId())){
            return error("参数错误！");
        }
        goods.setUpdateBy(getUsername());
        if (goodsService.edit(goods) == 0){
            return error("编辑失败，请联系管理员！");
        }
        return success("操作成功");
    }
    //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:goods:del')")
    @PostMapping("/del")
    public AjaxResult del(@RequestBody Goods goods)
    {
        if (StringUtils.isNull(goods.getId())){
            return error("参数错误！");
        }
        if (goodsService.del(goods.getId()) == 0){
            return error("删除失败，请联系管理员！");
        }
        return success("操作成功");
    }
}
