package org.smartx.summer.register.impl;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

import org.smartx.summer.config.register.MessageConvertersRegister;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binglin on 2016/10/28.
 * @author binglin
 */

public class FastJsonMessageConverterRegister implements MessageConvertersRegister {
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
