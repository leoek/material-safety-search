package mss.domain.responses;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Response class for formatting a custom response
 *
 * @param <T> Items of page, automatically filled by Spring
 */
public class PageResponse<T> {

    private List<T> items;
    private List<SingleFacetResponse> facets;
    private PageMeta meta;

    public PageResponse(List<T> items, PageMeta meta) {
        this.items = items;
        this.meta = meta;
    }

    public PageResponse(FacetPage<T> page, Page<FacetFieldEntry> resultFacetPage) {
        this.items = page.getContent();

        this.facets = resultFacetPage.getContent().stream().map(facetFieldEntry -> new SingleFacetResponse(facetFieldEntry.getField().toString(), facetFieldEntry.getValue(), facetFieldEntry.getValueCount())).collect(Collectors.toList());

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

    public List<SingleFacetResponse> getFacets() {
        return facets;
    }
}

