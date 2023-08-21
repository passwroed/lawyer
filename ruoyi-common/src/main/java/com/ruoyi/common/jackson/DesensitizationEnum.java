package com.ruoyi.common.jackson;

import java.util.function.Function;

/**
 * @ClassName : DesensitizationEnum
 * @Description : 脱敏枚举
 * @Author : WANGKE
 * @Date: 2023-08-17 18:57
 */
public enum DesensitizationEnum {
    /**
     * 名称脱敏
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2"))
            ,
    /**
     * 手机号脱敏
     */
    MOBILE_PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"))
            ,
    /**
     *地址脱敏
     */
    ADDRESS(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****"))
    ;

    /**
     * 成员变量  是一个接口类型
     */
    private Function<String, String> function;

    DesensitizationEnum(Function<String, String> function) {
        this.function = function;
    }

    public Function<String, String> function() {
        return this.function;
    }

}
