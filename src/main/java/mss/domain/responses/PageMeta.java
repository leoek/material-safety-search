package mss.domain.responses;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Class for formatting a custom meta object
 */
public class PageMeta {

    private Long totalCount;
    private int pageSize;
    private int count;
    private int page;
    private Boolean hasNext;
    private int totalPages;

    @JsonCreator
    public PageMeta(){}

    public PageMeta(int pageSize, int page, int totalPages, int count, Long totalCount, Boolean hasNext){
        this.pageSize = pageSize;
        this.page = page;
        this.totalPages = totalPages;
        this.count = count;
        this.totalCount = totalCount;
        this.hasNext = hasNext;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

