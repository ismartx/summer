package org.smartx.summer.formatter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.smartx.summer.bean.XUserAgent;
import org.springframework.format.Formatter;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * <p> 默认处理x-user-agent header的formatter </p>
 *
 * <b>Creation Time:</b> 2016年11月1日
 *
 * @author binglin
 * @since summer 0.1
 */
public class DefaultXUserAgentHeaderFormatter implements Formatter<XUserAgent> {
    @Override
    public XUserAgent parse(String s, Locale locale) throws ParseException {
        return convert(s);
    }

    @Override
    public String print(XUserAgent xUserAgent, Locale locale) {
        return xUserAgent.toString();
    }

    /**
     * input ver=30;caller=app;ch=guangwang;mac=02:00:00:00:00:00;os=23|Lenovo
     * K50-t5;platform=android
     */
    public static XUserAgent convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        Stream.of(s.split(";")).filter(StringUtils::isNoneBlank).forEach(x -> {
            String[] split = x.split("=");
            if (split.length < 2) {
                return;
            }
            map.put(split[0], split[1]);
        });
        XUserAgent xUserAgent = new XUserAgent();
        try {
            BeanUtils.populate(xUserAgent, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
        return xUserAgent;
    }
}
