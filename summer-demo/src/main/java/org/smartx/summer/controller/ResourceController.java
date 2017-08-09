package org.smartx.summer.controller;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.annotation.EnableCache;
import org.smartx.summer.annotation.VerifyJwtRole;
import org.smartx.summer.session.SessionAndTokenConstants;
import org.smartx.summer.session.TokenProvider;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Ming on 2016/11/3.
 */
@Api(value = "resource", description = "具体资源类")
@RestController
@RequestMapping("api")
@PropertySource("classpath:app.properties")
public class ResourceController {

    private static final Logger logger = LoggerFactory.getLogger(BaseResourceController.class);

    @Resource
    private TokenProvider tokenProvider;

    @ApiOperation(value = "退出", notes = "要求 Token")
    @GetMapping("user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute(SessionAndTokenConstants.TOKEN_CLAIMS);
        boolean isAudHasTag = tokenProvider.hasTag(claims.getAudience());
        if (isAudHasTag) {
            tokenProvider.delUserCurrentSession(claims);
        } else {
            tokenProvider.delTokenIdInSession(claims);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "获取具体资源", notes = "要求 Token，支持缓存")
    @EnableCache
    @GetMapping("user/resource")
    public ResponseEntity<?> resource(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute(SessionAndTokenConstants.SESSION_USER_ID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resource", "resource");
        jsonObject.put("userId", userId);
        return ResponseEntity.ok(jsonObject);
    }

    @ApiOperation(value = "获取admin资源", notes = "要求 admin 账号")
    @VerifyJwtRole(roles = {"admin", "user"})
    @GetMapping("user/admin-resource")
    public ResponseEntity<?> adminResourcece(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute(SessionAndTokenConstants.SESSION_USER_ID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resource", "admin-resource");
        jsonObject.put("userId", userId);
        return ResponseEntity.ok(jsonObject);
    }

    /*
    @ApiOperation(value = "获取当前用户的sessionkey", notes = "获取当前用户的sessionkey", response = TreeSet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "没有返回错误消息", response = Void.class),
            @ApiResponse(code = 400, message = "code=400,msg=错误请求", response = State.class)})
    @GetMapping("user/get-online-key")
    public TreeSet<String> onLineKey(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute(SessionAndTokenConstants.TOKEN_CLAIMS);
        String subject = claims.getSubject();
        logger.info("subject:{}", subject);
        return tokenProvider.getUserAllSessionKey(subject);
    }
    */

    /*
    @ApiOperation(value = "删除当前用户的所有key")
    @GetMapping("user/deleAllKey")
    public ResponseEntity<?> deleAllKey(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute(SessionAndTokenConstants.TOKEN_CLAIMS);
        String subject = claims.getSubject();
        logger.info("subject:{}", subject);
        tokenProvider.delUserAllSession(subject);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "200");
        return ResponseEntity.ok(jsonObject);

    }
    */
}
