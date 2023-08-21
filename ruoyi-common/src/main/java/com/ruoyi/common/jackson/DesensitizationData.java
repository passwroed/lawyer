package com.ruoyi.common.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @ClassName : DesensitizationData
 * @Description :
 * @Author : WANGKE
 * @Date: 2023-08-17 19:03
 */

@Target({ElementType.FIELD}) //表明作用在字段上
@Retention(RetentionPolicy.RUNTIME) //什么情况下生效
@JacksonAnnotationsInside //标明序列化
@JsonSerialize(using = DesensitizationDataSerialize.class)
public @interface DesensitizationData {
    DesensitizationEnum function();
}
