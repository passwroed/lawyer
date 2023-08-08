package com.ruoyi.web.controller.lawyer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.lawyer.Area;
import com.ruoyi.system.service.laywer.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

/**
 * @ClassName : AreaController
 * @Description : 测试
 * @Author : WANGKE
 * @Date: 2023-07-18 11:58
 */
@RestController
@RequestMapping("/area")
public class AreaController extends BaseController {
    @Autowired
    private AreaService areaService;

    @PostMapping("/add")
    public void add()
    {
        String str = "";
        String Path="/Users/wangke/outher_project/wcc/lawyer/lawyer/area_format_array.json";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                str += tempString;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject demo= JSON.parseObject(str);
        JSONArray o=demo.getJSONArray("array");
        for (int i = 0; i < o.size(); i++) {
            JSONObject a = o.getJSONObject(i);
            Area area = new Area();
            area.setId(a.getInteger("i"));
            area.setPid(a.getInteger("p"));
            area.setName(a.getString("n"));
            areaService.add(area);
            System.out.println(a.getInteger("i"));
        }
    }

    @PostMapping("/list")
    public TableDataInfo list(@Validated @RequestBody Area area)
    {
//        startPage();
        List<Area> list = areaService.list(area);
        return getDataTable(list);
    }
    @PostMapping("/info")
    public AjaxResult Info(@RequestBody Area area)
    {
        return success(areaService.Info(Long.valueOf(area.getId())));
    }
}
