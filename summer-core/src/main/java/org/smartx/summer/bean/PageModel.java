package org.smartx.summer.bean;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * <b>Creation Time:</b> 2016/12/26
 *
 * @author binglin
 */
public class PageModel<E> {

    private Integer page;

    private Integer size;

    private List<E> elements = Collections.emptyList();

    private Integer totalCount = 0;

    public PageModel() {
    }

    public PageModel(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public PageModel(Pageable pageable) {
        this.page = pageable.getPage();
        this.size = pageable.getSize();
    }

    public Integer getTotalPage() {
        if (0 == size) {
            return 0;
        }
        return Double.valueOf(Math.ceil((float) totalCount / size)).intValue();
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<E> getElements() {
        return elements;
    }

    public void setElements(List<E> elements) {
        this.elements = elements;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
