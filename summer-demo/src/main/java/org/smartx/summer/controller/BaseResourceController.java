package org.smartx.summer.controller;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.bean.LoginDTO;
import org.smartx.summer.bean.State;
import org.smartx.summer.bean.User;
import org.smartx.summer.bean.XUserAgent;
import org.smartx.summer.exception.AuthenticationException;
import org.smartx.summer.session.SessionAndTokenConstants;
import org.smartx.summer.session.SessionManager;
import org.smartx.summer.session.SessionUser;
import org.smartx.summer.session.TokenProvider;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;

/**
 * Created by Ming on 2016/10/22.
 */
@RestController
@RequestMapping("api")
@PropertySource("classpath:app.properties")
public class BaseResourceController {

    @Resource
    private SessionManager sessionManager;

    @Resource
    private TokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(BaseResourceController.class);

    private Map<String, User> userMap = new HashMap<>();

    @PostConstruct
    public void dataInit() {
        User admin = new User();
        admin.setPassword("admin1");
        admin.setUserPhone("01234567891");
        admin.setId(1);
        admin.setRole("user,admin");

        User user = new User();
        user.setPassword("user11");
        user.setUserPhone("01234567892");
        user.setId(2);
        user.setRole("user");

        userMap.put("01234567891", admin);
        userMap.put("01234567892", user);
    }

    @ApiOperation(value = "注册",response = LoginDTO.class)
    @PostMapping("base/register")
    public ResponseEntity<?> register(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return ResponseEntity.ok(loginDTO);
    }

    @CrossOrigin(methods = RequestMethod.POST)
    @ApiOperation(value = "身份认证", response = State.class, notes = "admin 账号:{\"password\":\"admin1\",\"userPhone\":\"01234567891\"}\n" +
            "普通用户账号:{\"password\":\"user11\",\"userPhone\":\"01234567892\"}")
    @PostMapping(value = "base/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginDTO,
                                          @RequestHeader("X-User-Agent") XUserAgent userAgent,
                                          HttpServletRequest request, HttpServletResponse response) {
        String userPhone = loginDTO.getUserPhone();
        User loginUser = userMap.get(userPhone);
        if (loginUser == null) {
            throw new AuthenticationException("User Not Exist");
        }

        if (!loginUser.getPassword().equals(loginDTO.getPassword())) {
            throw new AuthenticationException("Error Password");
        }

        String audience = userAgent.getCaller();
        if (StringUtils.isBlank(audience) || !tokenProvider.isValidateAudience(audience)) {
            return ResponseEntity.ok(new State(400, "error audience"));
        }

        String sessionKey = tokenProvider.generateSessionKey(audience, userPhone);
        String tokenIdInSession = tokenProvider.getTokenIdFromSession(sessionKey);
        if (null != tokenIdInSession) {
            logger.info("重复登陆,sessionKey:{},userPhone:{}", sessionKey, userPhone);
        }

        String jti = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase(); //token 的id ,可自定义，比如使用时间戳
        String tokenAudience = tokenProvider.buildAudIfNeed(audience, sessionKey);
        String jwt = tokenProvider.createJwt(userPhone, new Date(), jti, loginUser.getRole(), tokenAudience);

        SessionUser sessionUser = new SessionUser(loginUser.getId().toString(), jti);
        sessionManager.setSessionUser(sessionKey, sessionUser);
        tokenProvider.flushTokenExpireTimeBySessionKeyAndAud(sessionKey, audience);

        response.addHeader(SessionAndTokenConstants.AUTHORIZATION_HEADER, "Bearer ".concat(jwt));
        logger.info("来自:{} 设备的:{} 用户 认证成功, session key:{} ", audience, userPhone, sessionKey);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", "Bearer ".concat(jwt));
        return ResponseEntity.ok(jsonObject);
    }
}
