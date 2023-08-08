package com.ruoyi.system.domain.lawyer;

import javax.validation.constraints.NotNull;

/**
 * @ClassName : Area
 * @Description : 区划
 * @Author : WANGKE
 * @Date: 2023-07-18 11:32
 */
public class Area {

    private int id;
    @NotNull(message = "pid不能为空")
    private int pid;
    private String name;

    private Area child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getChild() {
        return child;
    }

    public void setChild(Area child) {
        this.child = child;
    }
}
