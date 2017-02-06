package org.smartx.summer.filter;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.bean.State;
import org.smartx.summer.common.GetRequestIpUtil;
import org.smartx.summer.session.SessionAndTokenConstants;
import org.smartx.summer.session.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;

/**
 * <p> Jwt 过滤器, 可以对指定 url 进行拦截 </p>
 *
 * <b>Creation Time:</b> 2016年10月24日
 *
 * @author Ming
 * @since summer 0.1
 */
public class JwtTokenAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthFilter.class);

    @Resource
    private TokenProvider tokenProvider;

    //可在注入时候配置
    private Set<Pattern> excludeUrl;

    public void setExcludeUrl(Set<Pattern> excludeUrl) {
        this.excludeUrl = excludeUrl;
    }

    private boolean isExclude(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (CollectionUtils.isEmpty(excludeUrl)) {
            return false;
        }
        boolean result = excludeUrl.parallelStream().anyMatch((s) -> s.matcher(uri).find());
        logger.debug("request uri:{},result:{}", uri, result);
        return result;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isExclude(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(SessionAndTokenConstants.AUTHORIZATION_HEADER);

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("did not find token in header,request:{},ip:{}", request.getRequestURL(), GetRequestIpUtil.getRemoteIP(request));
            convertMsgToJson(response, TokenProvider.DEFAULT_INVALID_JWT_MSG);
            return;
        }

        final String token = authorizationHeader.substring(7); // The part after "Bearer "

        try {
            final Claims claims = tokenProvider.getClaimsFromToken(token);
            tokenProvider.verifyToken(claims);
            Integer userId = tokenProvider.getUserIdByClaims(claims);
            request.setAttribute(SessionAndTokenConstants.SESSION_USER_ID, userId);
            request.setAttribute(SessionAndTokenConstants.TOKEN_CLAIMS, claims);
            filterChain.doFilter(request, response);
        } catch (SignatureException exception) {
            logger.warn("JWT signature does not match locally computed signature,token:{}", token);
            convertMsgToJson(response, TokenProvider.TOKEN_INVALID_SIGNATURE);
        } catch (final Exception e) {
            logger.warn("Could not get claims,invalid token,IP:{},Cause:{}", GetRequestIpUtil.getRemoteIP(request), e.getMessage());
            convertMsgToJson(response, TokenProvider.DEFAULT_EXPIRE_TOKEN_MSG);
        }
    }

    public void convertMsgToJson(HttpServletResponse response, String errMsg) throws IOException {
        State errorResponse = new State(HttpStatus.UNAUTHORIZED.value(), errMsg);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(JSON.toJSON(errorResponse));
    }
}
