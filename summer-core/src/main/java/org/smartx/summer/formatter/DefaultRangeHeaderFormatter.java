package org.smartx.summer.formatter;

import org.apache.commons.lang3.StringUtils;
import org.smartx.summer.bean.Pageable;
import org.springframework.format.Formatter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * <p> 默认处理Range header的formatter </p>
 *
 * <b>Creation Time:</b> 2016年10月31日
 *
 * @author binglin
 * @since summer 0.1
 */
public class DefaultRangeHeaderFormatter implements Formatter<Pageable> {

    private static final int LENGTH = 2;

    @Override
    public Pageable parse(String s, Locale locale) throws ParseException {
        return convert(s);
    }

    @Override
    public String print(Pageable pageable, Locale locale) {
        return pageable.toString();
    }

    /**
     * convert "sort=name,order=desc;sort=age,order=asc;page=0,size=10" to a Pageable
     *
     * @return Pageable
     */
    public static Pageable convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return doConvert(s);
    }

    private static Pageable doConvert(String s) {
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        Stream.of(s.split(";")).filter(StringUtils::isNoneBlank).forEach(x -> {
            String[] var = x.split(",");
            Stream.of(var).filter(StringUtils::isNoneBlank).forEach(y -> {
                String[] var1 = y.split("=");
                if (var1.length < LENGTH) {
                    return;
                }
                multiValueMap.add(var1[0].trim(), var1[1].trim());
            });
        });
        Integer page = StringUtils.isBlank(multiValueMap.getFirst("page")) ? 0 : Integer.valueOf(multiValueMap.getFirst("page"));
        Integer size = StringUtils.isBlank(multiValueMap.getFirst("size")) ? 10 : Integer.valueOf(multiValueMap.getFirst("size"));
        Pageable pageable = new Pageable(page, size);
        pageable.setSorts(doConvertSort(multiValueMap));
        return pageable;
    }

    private static List<Pageable.Sort> doConvertSort(MultiValueMap<String, String> multiValueMap) {
        List<String> sorts = multiValueMap.get("sort");
        List<Pageable.Sort> sortList = new LinkedList<>();
        if (null == sorts) {
            return null;
        } else if (1 == sorts.size()) {
            Pageable.Sort sort = new Pageable.Sort(sorts.get(0), Pageable.Direction.fromStringDefaultAsc(multiValueMap.getFirst("order")));
            sortList.add(sort);
        } else {
            for (int i = 0; i < sorts.size(); i++) {
                List<String> orders = multiValueMap.get("order");
                Pageable.Sort sort = new Pageable.Sort(sorts.get(i), Pageable.Direction.fromStringDefaultAsc(orders.get(i)));
                sortList.add(sort);
            }
        }
        return sortList;
    }
}
