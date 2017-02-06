package org.smartx.summer.bean;

import java.util.Map;

/**
 * <p> 组合接口请求 </p>
 *
 * <b>Creation Time:</b> 2016年10月20日
 *
 * @author binglin
 * @since summer 0.1
 */
public class CombineRequest {

    private String url;

    private String method;

    private String requestId;

    private String version;

    private Map<String, Object> param;

    private Map<String, Object> header;

    private String eTag;

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
