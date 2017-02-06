package org.smartx.summer.bean;

/**
 * <p> 组合接口返回 </p>
 *
 * <b>Creation Time:</b> 2016年10月20日
 *
 * @author binglin
 * @since summer 0.1
 */
public class CombineResponse {

    private String requestId;

    private Integer httpStatus;

    private Object entity;

    private String eTag;

    public CombineResponse() {
    }

    public CombineResponse(String requestId, Integer httpStatus) {
        this.requestId = requestId;
        this.httpStatus = httpStatus;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }
}
