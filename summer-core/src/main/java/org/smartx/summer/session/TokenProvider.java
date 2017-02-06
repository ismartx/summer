package org.smartx.summer.session;

import com.alibaba.fastjson.JSON;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.exception.AuthenticationException;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.Resource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * <p> TokenProvider 提供了对 Jwt 常用的方法 </p>
 *
 * <b>Creation Time:</b> 2016年10月24日
 *
 * @author Ming
 * @since summer 0.1
 */
public class TokenProvider {

    @Resource
    private SessionManager sessionManager;

    //Token 的属性在注入 TokenProvider 时定义
    private String secret = "YOUR_TOKEN_SECRET";

    private String audienceExpireTime = "APP:0;WEB:1800";

    private String tokenAudSupportTag = "WEB";

    private Integer tokenTagMaxNum = 20;

    public static final String DEFAULT_INVALID_JWT_MSG = "Invalid token";

    public static final String DEFAULT_EXPIRE_TOKEN_MSG = "Token is expired";

    public static final String TOKEN_INVALID_SIGNATURE = "Token invalid signature";

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public String createJwt(String subject, Date date, String jti, String roles, String audience) {
        Assert.notNull(subject);
        Assert.notNull(date);
        Assert.notNull(jti);
        Assert.notNull(audience);
        Assert.notNull(roles);
        return Jwts
                .builder()
                .setSubject(subject)
                .setAudience(audience.toUpperCase())
                .claim(SessionAndTokenConstants.TOKEN_ROLE_KEY, roles)
                .setIssuedAt(date)
                .setId(jti)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public String createJwt(String subject, Date date, String jti, String[] roles, String audience) {
        Assert.notNull(subject);
        Assert.notNull(date);
        Assert.notNull(jti);
        Assert.notNull(audience);
        Assert.notEmpty(roles);
        return Jwts
                .builder()
                .setSubject(subject)
                .setAudience(audience.toUpperCase())
                .claim(SessionAndTokenConstants.TOKEN_ROLE_KEY, StringUtils.join(Arrays.asList(roles), ","))
                .setIssuedAt(date)
                .setId(jti)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public String createJwt(String subject, Date date, String jti, List<String> roles, String audience) {
        String[] array = roles.toArray(new String[roles.size()]);
        return createJwt(subject, date, jti, array, audience);
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从JWT 的Claims 中可以获取到用户的 sessionkey
     */
    public String getSessionKeyFromClaims(final Claims claims) {
        Assert.notNull(claims, "Claims  could not be null");
        final String aud = claims.getAudience();
        final String sub = claims.getSubject();
        return SessionAndTokenConstants.SESSION_KEY_PREFIX.concat(sub).concat(":").concat(aud.toUpperCase());
    }

    public String[] getRolesFromClaims(final Claims claims) {
        Assert.notNull(claims, "Claims  could not be null");
        String roleString = (String) claims.get(SessionAndTokenConstants.TOKEN_ROLE_KEY);
        Assert.notNull(roleString);
        return roleString.split(",");
    }

    public String getTokenIdFromClaims(final Claims claims) {
        return claims.getId();
    }

    public void verifyToken(final Claims claims) {
        final String sessionKey = getSessionKeyFromClaims(claims);
        String tokenId = getTokenIdFromClaims(claims);
        if (!verifyTokenId(sessionKey, tokenId)) {
            logger.warn("token is expire,sessionKey:{},aud:{},issueAt:{},tokenId:{},tokenIdInSession:{}", sessionKey, claims.getAudience(), claims.getIssuedAt(),
                    tokenId, getTokenIdFromSession(sessionKey));
            throw new AuthenticationException(DEFAULT_EXPIRE_TOKEN_MSG);
        }

        //校验完后，如果该 token 的接收端有设置 token 有效期，则重新设置 token 的 expireTime
        String aud = claims.getAudience();
        Integer expireTime = getExpireTimeByAudience(aud);
        if (null != expireTime && expireTime > 0) {
            Assert.isTrue(flushTokenExpireTime(sessionKey, expireTime));
        }
    }

    public String generateSessionKey(final String audience, final String subject) {
        return isSetAudienceSupportTag(audience) ? generateSessionKeyWithTag(audience, subject) : generateSessionKeyWithoutTag(audience, subject);
    }

    public String generateSessionKeyWithoutTag(final String audience, final String subject) {
        Assert.notNull(audience, "miss audience field");
        Assert.notNull(subject, "miss subject field");
        return SessionAndTokenConstants.SESSION_KEY_PREFIX.concat(subject).concat(":").concat(audience.toUpperCase());
    }

    public String generateSessionKeyWithTag(final String audience, final String subject) {
        String key = generateSessionKeyWithoutTag(audience, subject).concat(":TAG:");
        for (int i = 1; i <= tokenTagMaxNum; i++) {
            String t = key.concat(Integer.toString(i));
            if (!sessionManager.isExistSessionKey(t)) {
                return t;
            }
        }
        throw new RuntimeException("The session number of " + subject + " is more than require");
    }

    public String getTokenIdFromSession(final String key) {
        return sessionManager.getSessionField(key, SessionAndTokenConstants.SESSION_USER_TOKEN_ID);
    }

    public boolean verifyTokenId(final String key, String jti) {
        String tokenId = getTokenIdFromSession(key);
        return !StringUtils.isEmpty(tokenId) && tokenId.equals(jti);
    }

    public Boolean delTokenIdInSession(final Claims claims) {
        String key = getSessionKeyFromClaims(claims);
        return delTokenIdInSession(key);
    }

    public Boolean delTokenIdInSession(final String key) {
        return sessionManager.delSessionField(key, SessionAndTokenConstants.SESSION_USER_TOKEN_ID);
    }

    public void delUserCurrentSession(final String key) {
        sessionManager.deleteSessionByKey(key);
    }

    public void delUserCurrentSession(final Claims claims) {
        String key = getSessionKeyFromClaims(claims);
        delUserCurrentSession(key);
    }

    public boolean flushTokenExpireTime(final String key, int seconds) {
        return sessionManager.setSessionExpire(key, seconds);
    }

    /**
     * 刷新key 的存活时间，如果为 0 的话，不超时
     */
    public void flushTokenExpireTimeBySessionKeyAndAud(final String key, String aud) {
        Integer expireTime = getExpireTimeByAudience(aud);
        if (null != expireTime && expireTime > 0) {
            Assert.isTrue(flushTokenExpireTime(key, expireTime));
        }
    }

    /**
     * 从配置属性 audienceExpireTime 中获取指定设备超时信息
     */
    public Integer getExpireTimeByAudience(String audience) {
        logger.debug("audienceExpireTime:{},audience:{}", audienceExpireTime, audience);
        Optional<String> value = Stream.of(audienceExpireTime.toUpperCase().split(";")).filter(x -> x.startsWith(audience.toUpperCase())).findAny();
        if (value.isPresent()) {
            String result = value.get().replaceAll("[^0-9]", "");
            return Integer.valueOf(result);
        } else {
            return 1800;
        }
    }

    public boolean isSetAudienceSupportTag(String audience) {
        Assert.notNull(audience, "miss audience field");
        return Stream.of(tokenAudSupportTag.toUpperCase().split(";")).filter(x -> x.equalsIgnoreCase(audience)).findAny().isPresent();
    }

    public boolean isValidateAudience(String audience) {
        Assert.notNull(audience, "miss audience field");
        logger.debug("setting audienceExpireTime:{},audience:{}", audienceExpireTime, audience);
        return Stream.of(audienceExpireTime.toUpperCase().split(";")).filter(x -> x.startsWith(audience.toUpperCase())).findAny().isPresent();
    }

    public String buildAudIfNeed(String audience, String sessionKey) {
        Matcher m = Pattern.compile(":TAG.*").matcher(sessionKey);
        return m.find() ? audience.concat(m.group(0)).toUpperCase() : audience.toUpperCase();
    }

    public boolean hasTag(String value) {
        Assert.notNull(value, "miss value field");
        return Pattern.compile(":TAG.*").matcher(value).find();
    }

    public Integer getUserIdFromSession(final String key) {
        return Integer.valueOf(sessionManager.getSessionField(key, SessionAndTokenConstants.SESSION_USER_ID));
    }

    public Integer getUserIdByClaims(Claims claims) {
        String sessionKey = getSessionKeyFromClaims(claims);
        return getUserIdFromSession(sessionKey);
    }

    public void delUserAllSession(String subject) {
        TreeSet<String> treeSet = getUserAllSessionKey(subject);
        if (!CollectionUtils.isEmpty(treeSet)) {
            logger.debug("all key:{}", JSON.toJSONString(treeSet));
            treeSet.forEach(this::delUserCurrentSession);
        }
    }

    public TreeSet<String> getUserAllSessionKey(String subject) {
        Assert.notNull(subject, "miss subject field");
        String pattern = SessionAndTokenConstants.SESSION_KEY_PREFIX.concat(subject).concat("*");
        return sessionManager.getKeysByPattern(pattern);
    }


    public void setTokenTagMaxNum(Integer tokenTagMaxNum) {
        this.tokenTagMaxNum = tokenTagMaxNum;
    }

    public void setTokenAudSupportTag(String tokenAudSupportTag) {
        this.tokenAudSupportTag = tokenAudSupportTag;
    }

    public void setAudienceExpireTime(String audienceExpireTime) {
        this.audienceExpireTime = audienceExpireTime;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAudienceExpireTime() {
        return audienceExpireTime;
    }

    public String getSecret() {
        return secret;
    }

    public String getTokenAudSupportTag() {
        return tokenAudSupportTag;
    }

    public Integer getTokenTagMaxNum() {
        return tokenTagMaxNum;
    }

}
