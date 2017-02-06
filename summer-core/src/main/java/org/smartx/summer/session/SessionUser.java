package org.smartx.summer.session;

/**
 * <p>  </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author Ming
 * @since summer 0.1
 */
public class SessionUser {

    private String uid;

    private String jti;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public SessionUser() {
        super();
    }

    public SessionUser(String uid, String jti) {
        super();
        this.uid = uid;
        this.jti = jti;
    }
}
