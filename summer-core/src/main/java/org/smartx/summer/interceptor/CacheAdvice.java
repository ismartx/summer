package org.smartx.summer.interceptor;

import org.smartx.commons.utils.JsonUtils;
import org.smartx.summer.annotation.EnableCache;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p> 对缓存接口的支持（即标注了@EnableCache），在接口执行完以后，还没经过HttpMessageConverter转换之前执行
 * 这样可以修改标注为@RestController的类或者返回值是ResponseEntity<>的接口进行返回值得修改 </p>
 *
 * <b>Creation Time:</b> 2016年10月22日
 *
 * @author binglin
 * @since summer 0.1
 */
@ControllerAdvice
public class CacheAdvice implements ResponseBodyAdvice<Object> {

    private static final String GET = "GET";

    private static final String HEAD = "HEAD";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getMethod().getDeclaredAnnotation(EnableCache.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String etag = serverHttpRequest.getHeaders().getFirst(HttpHeaders.IF_NONE_MATCH);
        if (!((GET.equals(serverHttpRequest.getMethod().name())) || (HEAD.equals(serverHttpRequest.getMethod().name())))) {
            return o;
        }
        String md5;
        if (o instanceof ResponseEntity) {
            ResponseEntity response = (ResponseEntity) o;
            md5 = DigestUtils.md5DigestAsHex(JsonUtils.toJsonString(response.getBody()).getBytes());
        } else {
            md5 = DigestUtils.md5DigestAsHex(JsonUtils.toJsonString(o).getBytes());
        }
        serverHttpResponse.getHeaders().add("Etag", md5);
        if (md5.equalsIgnoreCase(etag)) {
            serverHttpResponse.setStatusCode(HttpStatus.NOT_MODIFIED);
            return null;
        } else {
            return o;
        }
    }
}
