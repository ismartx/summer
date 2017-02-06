package org.smartx.summer.session;

/**
 * Created by Ming on 2016/11/10.
 */
public interface SessionAndTokenConstants {

    String SESSION_KEY_PREFIX = "SESSION:";

    String SESSION_USER_ID = "uid";

    String SESSION_USER_LOGIN_IP = "loginIp";

    String SESSION_USER_CLIENT = "client";

    String SESSION_USER_TOKEN_ID = "jti";

    String AUTHORIZATION_HEADER = "Authorization";

    String TOKEN_ROLE_KEY = "roles";

    String TOKEN_CLAIMS = "claims";

    enum audience {
        WEB, MOBILE, WEIXIN, TABLET
    }

}
