package mss.domain.responses;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Response class for formatting a custom response
 * @param <T> Items of page, automatically filled by Spring
 */
public class PageResponse<T> {

    private List<T> items;
    private PageMeta meta;

    public PageResponse(List<T> items, PageMeta meta){
        this.items = items;
        this.meta = meta;
    }

    public PageResponse(Page<T> page){
        this.items = page.getContent();
        this.meta = new PageMeta(
                page.getSize(),
                page.getNumber(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.hasNext()
        );
    }

    public PageMeta getMeta() {
        return meta;
    }

    public List getItems() {
        return items;
    }
}

