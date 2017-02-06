package org.smartx.summer.config.register.impl;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

import org.smartx.summer.config.register.MessageConvertersRegister;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 默认fastjson消息converter </p>
 *
 * <b>Creation Time:</b> 2016年10月28日
 *
 * @author binglin
 * @since summer 0.1
 */
@Component
public class DefaultFastJsonMessageConverterRegister implements MessageConvertersRegister {
    @Override
    public void registry(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter4();
        ArrayList<MediaType> arrayList = new ArrayList<MediaType>() {{
            add(MediaType.APPLICATION_JSON_UTF8);
            add(MediaType.valueOf("text/html;charset=UTF-8"));
            add(MediaType.MULTIPART_FORM_DATA);
        }};
        fastJsonHttpMessageConverter.setSupportedMediaTypes(arrayList);
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.QuoteFieldNames,
                SerializerFeature.DisableCircularReferenceDetect);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonHttpMessageConverter);
    }
}
