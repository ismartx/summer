package org.smartx.summer.controller;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.commons.utils.JsonUtils;
import org.smartx.summer.bean.CombineRequest;
import org.smartx.summer.bean.CombineResponse;
import org.smartx.summer.common.SpringMvcContextHolder;
import org.smartx.summer.formatter.DefaultRangeHeaderFormatter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

/**
 * <p> 组合接口controller </p>
 *
 * <b>Creation Time:</b> 2016年10月20日
 *
 * @author binglin
 * @since summer 0.1
 */
@RestController
@RequestMapping("combine")
public class CombineController {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private static final Logger logger = LoggerFactory.getLogger(CombineController.class);

    @PostMapping
    public ResponseEntity<List<CombineResponse>> combine(@RequestBody List<CombineRequest> combineRequests) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Map<CombineRequest, HandlerMethod> handlerMethodMap = new HashMap<>();
        //利用传入的CombineRequest找到相应的handleMethod
        handlerMethods.entrySet().forEach(ms -> {
            combineRequests.forEach(req -> {
                String url = req.getUrl().startsWith("/") ? req.getUrl() : "/" + req.getUrl();
                List<String> matchingPatterns = ms.getKey().getPatternsCondition().getMatchingPatterns(url);
                Set<RequestMethod> methods = ms.getKey().getMethodsCondition().getMethods();
                HeadersRequestCondition headersCondition = ms.getKey().getHeadersCondition();
                Set<NameValueExpression<String>> expressions = headersCondition.getExpressions();
                if (matchingPatterns != null && matchingPatterns.size() > 0 && methods.contains(RequestMethod.valueOf(req.getMethod()))) {


                    //判断：1. 组合接口没有指定version值，并且当前的HandleMethod的RequestMappingInfo也没有header值
                    //     2. 组合接口指定的version的值为v1，并且当前的HandleMethod的RequestMappingInfo也没有指定header值
                    if ((null == req.getVersion() && 0 == expressions.size())
                            || (0 == expressions.size() && "v1".equalsIgnoreCase(req.getVersion()))) {
                        //没有header要求
                        handlerMethodMap.put(req, ms.getValue());
                    } else if (null == req.getVersion() && expressions.size() > 0) {
                        Map<String, Object> requestMappingInfoHeaderMap = expressions.stream().collect(
                                Collectors.toMap(NameValueExpression::getName, NameValueExpression::getValue)
                        );
                        // 组合接口没有指定version值，但是当前HandleMethod的RequestMappingInfo的header的version值为v1
                        //由于默认使用v1的方法，所以满足条件
                        if ("v1".equalsIgnoreCase(requestMappingInfoHeaderMap.get("version").toString())) {
                            handlerMethodMap.put(req, ms.getValue());
                        }
                    } else if (req.getVersion() != null && expressions.size() > 0) {
                        //当前handleMethod的RequestMappingInfo知道的header的值
                        Map<String, Object> requestMappingInfoHeaderMap = expressions.stream().collect(
                                Collectors.toMap(NameValueExpression::getName, NameValueExpression::getValue)
                        );
                        Map<String, Object> positionMap = new HashMap<>();
                        positionMap.put("version", req.getVersion());

                        if (positionMap.equals(requestMappingInfoHeaderMap)) {
                            handlerMethodMap.put(req, ms.getValue());
                        }
                    }
                }
            });
        });
        List<CombineResponse> combineResponses = new ArrayList<>(combineRequests.size());
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        //执行handleMethod
        combineRequests.parallelStream().forEach(req -> {
            CombineResponse combineResponse = new CombineResponse(req.getRequestId(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            HandlerMethod handlerMethod = handlerMethodMap.get(req);
            if (null == handlerMethod) {
                //没找到这个接口
                combineResponse.setHttpStatus(HttpStatus.NOT_FOUND.value());
            } else {
                Object object = SpringMvcContextHolder.getBean(handlerMethod.getBean().toString());
                Method method = handlerMethod.getMethod();
                Object result = null;

                String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
                Object[] objects = new Object[]{};
                objects = getParams(parameterNames, req, method, objects);
                try {
                    result = MethodUtils.invokeMethod(object, method.getName(), objects);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    logger.error("invoke method error method={} , request={}", handlerMethod, req, e);
                    combineResponses.add(combineResponse);
                    return;
                }
                if (result instanceof ResponseEntity) {
                    ResponseEntity response = (ResponseEntity) result;
                    String eTag = DigestUtils.md5DigestAsHex(JsonUtils.toJsonString(response.getBody()).getBytes());
                    if (req.getETag() != null && req.getETag().equalsIgnoreCase(eTag)) {
                        combineResponse.setEntity(null);
                        combineResponse.setHttpStatus(HttpStatus.NOT_MODIFIED.value());
                    } else {
                        combineResponse.setEntity(response.getBody());
                        combineResponse.setHttpStatus(response.getStatusCodeValue());
                        combineResponse.setETag(eTag);
                    }
                } else {
                    String eTag = DigestUtils.md5DigestAsHex(JsonUtils.toJsonString(result).getBytes());
                    if (req.getETag() != null && req.getETag().equalsIgnoreCase(eTag)) {
                        combineResponse.setEntity(null);
                        combineResponse.setHttpStatus(HttpStatus.NOT_MODIFIED.value());
                    } else {
                        combineResponse.setEntity(result);
                        combineResponse.setHttpStatus(HttpStatus.OK.value());
                        combineResponse.setETag(eTag);
                    }
                }
            }
            combineResponses.add(combineResponse);
        });
        return ResponseEntity.ok(combineResponses);
    }

    private Object[] getParams(String[] parameterNames, CombineRequest req, Method method, Object[] objects) {

        LinkedList<Object> collect = Stream.of(parameterNames).map(p -> req.getParam() == null ? null
                : (req.getParam().get(p) == null ? req.getHeader().get(p) : req.getParam().get(p)))
                .collect(Collectors.toCollection(LinkedList::new));
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if (parameterAnnotations[i][j] instanceof RequestHeader) {
                    RequestHeader annotation = method.getParameters()[i].getAnnotation(RequestHeader.class);
                    if ("Range".equalsIgnoreCase(annotation.value())) {
                        Object o = req.getHeader().get("Range");
                        collect.remove(i);
                        collect.add(i, DefaultRangeHeaderFormatter.convert(o.toString()));
                    }

                }

            }

        }
        return collect.toArray(objects);
    }
}
