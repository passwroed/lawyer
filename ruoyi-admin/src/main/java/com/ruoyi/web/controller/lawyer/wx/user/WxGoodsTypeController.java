package com.ruoyi.web.controller.lawyer.wx.user;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.lawyer.GoodsType;
import com.ruoyi.system.service.laywer.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : WxGoodsTypeController
 * @Description : 商品类型
 * @Author : WANGKE
 * @Date: 2023-09-05 23:36
 */
@RestController
@RequestMapping("/wxuser/goodsType")
public class WxGoodsTypeController extends BaseController {
        @Autowired
        private GoodsTypeService goodsTypeService;

        //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:goodsType:list')")
        @Anonymous
        @PostMapping("/list")
        public TableDataInfo list(@RequestBody GoodsType goodsType)
        {
            startPage();
            List<GoodsType> list = goodsTypeService.list(goodsType);
            return getDataTable(list);
        }
        //新增
//    @PreAuthorize("@ss.hasPermi('lawyer:goodsType:add')")
        @PostMapping("/add")
        public AjaxResult add(@Validated @RequestBody GoodsType goodsType)
        {
            goodsType.setCreateBy(getUsername());
            if (goodsTypeService.add(goodsType) == 0){
                return error("新增新闻失败，请联系管理员！");
            }
            return success("操作成功");
        }
        //编辑
//    @PreAuthorize("@ss.hasPermi('lawyer:goodsType:edit')")
        @PostMapping("/edit")
        public AjaxResult edit(@Validated @RequestBody GoodsType goodsType)
        {
            if (StringUtils.isNull(goodsType)||StringUtils.isNull(goodsType.getId())){
                return error("参数错误！");
            }
            goodsType.setUpdateBy(getUsername());
            if (goodsTypeService.edit(goodsType) == 0){
                return error("编辑新闻失败，请联系管理员！");
            }
            return success("操作成功");
        }
        //删除
//    @PreAuthorize("@ss.hasPermi('lawyer:goodsType:del')")
        @PostMapping("/del")
        public AjaxResult del(@RequestBody GoodsType goodsType)
        {
            if (StringUtils.isNull(goodsType.getId())){
                return error("参数错误！");
            }
            if (goodsTypeService.del(goodsType.getId()) == 0){
                return error("删除新闻失败，请联系管理员！");
            }
            return success("操作成功");
        }
}
