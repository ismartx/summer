package org.smartx.summer.bean;

/**
 * <p> header中的x-user-agent </p>
 *
 * <b>Creation Time:</b> 2016年11月01日
 *
 * @author binglin
 * @since summer 0.1
 */
public class XUserAgent {
    private Integer ver;

    private String caller;

    private String ch;

    private String mac;

    private String os;

    private String platform;

    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "ver=" + ver +
                "; caller=" + caller +
                "; ch=" + ch +
                "; mac=" + mac +
                "; os=" + os +
                "; platform=" + platform;
    }

    public static class Builder {
        private XUserAgent xUserAgent;

        private Builder(XUserAgent xUserAgent) {
            this.xUserAgent = xUserAgent;
        }

        public static Builder empty() {
            return new Builder(new XUserAgent());
        }

        public Builder ver(Integer ver) {
            xUserAgent.setVer(ver);
            return this;
        }

        public Builder ver(String ver) {
            xUserAgent.setVer(Integer.valueOf(ver));
            return this;
        }

        public Builder platform(String platform) {
            xUserAgent.setPlatform(platform);
            return this;
        }

        public Builder caller(String caller) {
            xUserAgent.setCaller(caller);
            return this;
        }

        public Builder ch(String ch) {
            xUserAgent.setCh(ch);
            return this;
        }

        public Builder mac(String mac) {
            xUserAgent.setMac(mac);
            return this;
        }

        public Builder os(String os) {
            xUserAgent.setOs(os);
            return this;
        }
    }
}
