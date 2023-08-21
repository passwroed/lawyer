package com.ruoyi.common.jackson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;
/**
 * @ClassName : DesensitizationDataSerialize
 * @Description :
 * @Author : WANGKE
 * @Date: 2023-08-17 19:04
 */
public class DesensitizationDataSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizationEnum fuincation;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        DesensitizationData annotation = beanProperty.getAnnotation(DesensitizationData.class);
        if(Objects.nonNull(annotation) && Objects.equals(beanProperty.getType().getRawClass(),String.class)){
            this.fuincation = annotation.function();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(),beanProperty);
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(fuincation.function().apply(s));
    }

}
