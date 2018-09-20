package mss.domain.responses;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import java.util.Collection;
import java.util.List;

/**
 * Response class for formatting a custom response
 *
 * @param <T> Items of page, automatically filled by Spring
 */
public class PageResponse<T> {

    private List<T> items;
    private Collection<Page<? extends FacetEntry>> facets;
    private PageMeta meta;

    public PageResponse(List<T> items, PageMeta meta) {
        this.items = items;
        this.meta = meta;
    }

    public PageResponse(FacetPage<T> page) {
        this.items = page.getContent();
        this.facets = page.getAllFacets();
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

    public Collection<Page<? extends FacetEntry>> getFacets() {
        return facets;
    }
}

