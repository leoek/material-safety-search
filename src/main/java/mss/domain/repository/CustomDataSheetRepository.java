package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import mss.domain.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;

import java.util.List;

public interface CustomDataSheetRepository {
    public Page<DataSheetDocument> generalSearch(List<String> criteria, List<String> filters, Pageable pageable);

    public PageResponse generalSearchFacet(List<String> criteria, List<String> filters, Pageable pageable, Boolean facetForFsc);

    public FacetPage<DataSheetDocument> advancedSearchFacet(String query, List<String> filters, Pageable pageable);

    public List<DataSheetDocument> autocompleteList(String searchTerm, String field);

    public List<DataSheetDocument> autocompleteList(String searchTerm, String field, Integer count);
}
