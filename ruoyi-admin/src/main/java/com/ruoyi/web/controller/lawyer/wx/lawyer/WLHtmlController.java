package com.ruoyi.web.controller.lawyer.wx.lawyer;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Html;
import com.ruoyi.system.service.laywer.HtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName : HtmlController
 * @Description : HTML页面富文本
 * @Author : WANGKE
 * @Date: 2023-07-16 23:02
 */
@RestController
@RequestMapping("/wxLawyer/html")
public class WLHtmlController extends BaseController {
    @Autowired
    private HtmlService htmlService;

    //列表查询（条件查询）
//    @PreAuthorize("@ss.hasPermi('lawyer:html:list')")
    @Anonymous
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody Html html)
    {
        startPage();
        List<Html> list = htmlService.list(html);
        return getDataTable(list);
    }


}
