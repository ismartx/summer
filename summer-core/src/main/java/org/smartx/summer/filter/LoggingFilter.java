package org.smartx.summer.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 过滤所有请求，并打印请求和响应的具体细节 </p>
 *
 * <b>Creation Time:</b> 2016年11月3日
 *
 * @author binglin
 * @since summer 0.1
 */
public class LoggingFilter extends OncePerRequestFilter {

    protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_PREFIX = "Request: ";
    private static final String RESPONSE_PREFIX = "Response: ";
    private static ArrayList<Pattern> excludeUrl;
    private ThreadLocal<Boolean> isExclude = new ThreadLocal<>();

    public static final List<String> REQUEST_USEFUL_HEADERS = new ArrayList<String>() {{
        add("Request-Id");
        add("Range");
        add("If-None-Match");
        add("version");
        add("Authorization");
        add("X-User-Agent");
    }};

    public static void setExcludeUrl(String excludeUrl) {
        LoggingFilter.excludeUrl = Stream.of(excludeUrl.split(";")).map(x -> x.replaceAll("\\*", ".*")).map(Pattern::compile).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        request = new RequestWrapper(request);
        response = new ResponseWrapper(response);
        response.setHeader("Request-Id", request.getHeader("Request-Id"));

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("error to filter e={}", e);
        } finally {
            logRequest(request);
            logResponse((ResponseWrapper) response);
            this.isExclude.remove();
        }

    }

    private void logRequest(final HttpServletRequest request) {
        if (isExclude(request)) {
            return;
        }
        StringBuilder msg = new StringBuilder();
        msg.append(REQUEST_PREFIX);

        String queryString = request.getQueryString();
        String url = request.getRequestURL() + ((null == queryString) ? "" : ("?" + request.getQueryString()));
        Map<String, String> headers = REQUEST_USEFUL_HEADERS.stream().filter(x -> !StringUtils.isBlank(request.getHeader(x))).
                collect(Collectors.toMap(x -> x, request::getHeader));
        if (request instanceof RequestWrapper) {
            msg.append("request id=").append(request.getHeader("Request-Id")).append("; ");
        }

        if (request.getMethod() != null) {
            msg.append("method=").append(request.getMethod()).append("; ");
        }
        if (request.getContentType() != null) {
            msg.append("content type=").append(request.getContentType()).append("; ");
        }
        msg.append("url=").append(url).append("\n");
        msg.append("header=").append(headers);


        if (request instanceof RequestWrapper && !isMultipart(request) && !isBinaryContent(request)) {
            RequestWrapper requestWrapper = (RequestWrapper) request;
            try {
                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() :
                        "UTF-8";
                msg.append("\npayload=").append(new String(requestWrapper.toByteArray(), charEncoding));
            } catch (UnsupportedEncodingException e) {
                logger.warn("Failed to parse request payload", e);
            }

        }
        logger.info(msg.toString());
    }

    private boolean isBinaryContent(final HttpServletRequest request) {
        if (request.getContentType() == null) {
            return false;
        }
        return request.getContentType().startsWith("image") || request.getContentType().startsWith("video") || request.getContentType().startsWith("audio");
    }

    private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
    }

    private boolean isExclude(HttpServletRequest request) {
        String url = request.getRequestURI();
        boolean exclude = false;
        for (Pattern p : LoggingFilter.excludeUrl) {
            if (p.matcher(url).matches()) {
                exclude = true;
                break;
            }
        }
        this.isExclude.set(exclude);
        return exclude;
    }

    private void logResponse(final ResponseWrapper response) {
        if (this.isExclude.get()) {
            return;
        }
        StringBuilder msg = new StringBuilder();
        msg.append(RESPONSE_PREFIX);
        msg.append("status=").append(response.getStatus()).append(";");
        msg.append("request id=").append((response.getHeader("Request-Id"))).append("\n");
        try {
            msg.append("payload=").append(new String(response.toByteArray(), response.getCharacterEncoding()));
        } catch (UnsupportedEncodingException e) {
            logger.warn("Failed to parse response payload", e);
        }
        logger.info(msg.toString());
    }

}
