package org.smartx.summer.interceptor;

import org.smartx.summer.annotation.VerifyJwtRole;
import org.smartx.summer.exception.AuthenticationException;
import org.smartx.summer.session.SessionAndTokenConstants;
import org.smartx.summer.session.TokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;

/**
 * <p> 对使用了 {@link VerifyJwtRole} 注解的方法进行拦截。 </p>
 *
 * <b>Creation Time:</b> 2016年10月25日
 *
 * @author Ming
 * @since summer 0.1
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod maControl = (HandlerMethod) handler;
            VerifyJwtRole verifyJwtRole = maControl.getMethodAnnotation(VerifyJwtRole.class);

            if (null == verifyJwtRole) {
                return true;
            }

            Claims claims = null;
            if (verifyJwtRole.verifyJwt()) {
                String authorizationHeader = request.getHeader(SessionAndTokenConstants.AUTHORIZATION_HEADER);
                if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                    throw new AuthenticationException("Missing or invalid Authorization header.");
                }
                // The part after "Bearer "
                final String token = authorizationHeader.substring(7);
                try {
                    claims = tokenProvider.getClaimsFromToken(token);

                    tokenProvider.verifyToken(claims);
                    Integer userId = tokenProvider.getUserIdByClaims(claims);
                    request.setAttribute(SessionAndTokenConstants.SESSION_USER_ID, userId);
                    request.setAttribute(SessionAndTokenConstants.TOKEN_CLAIMS, claims);
                } catch (SignatureException exception) {
                    response.setHeader("WWW-Authenticate", "xBasic realm=\"fake\"");
                    throw new AuthenticationException(TokenProvider.TOKEN_INVALID_SIGNATURE);
                } catch (final Exception e) {
                    response.setHeader("WWW-Authenticate", "xBasic realm=\"fake\"");
                    throw new AuthenticationException(TokenProvider.DEFAULT_INVALID_JWT_MSG);
                }
            } else {
                claims = (Claims) request.getAttribute(SessionAndTokenConstants.TOKEN_CLAIMS);
            }

            Assert.notNull(claims, "Claims  could not be null");

            if (verifyJwtRole.roles().length > 0) {
                String[] requireRole = verifyJwtRole.roles();
                String[] roles = tokenProvider.getRolesFromClaims(claims);
                if (!Arrays.asList(roles).containsAll(Arrays.asList(requireRole))) {
                    // 没有方法要求的角色
                    throw new AuthenticationException("You are not the require role.");
                }
            }
            return true;
        }
        return true;
    }
}
