package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import java.util.List;

public class CustomDataSheetRepositoryImpl implements CustomDataSheetRepository {
    private static final Logger log = LoggerFactory.getLogger(CustomDataSheetRepositoryImpl.class);

    private SolrOperations solrTemplate;

    public CustomDataSheetRepositoryImpl() {
        super();
    }

    @Autowired
    public CustomDataSheetRepositoryImpl(SolrOperations solrTemplate) {
        super();
        this.solrTemplate = solrTemplate;
    }

    /**
     * Generates an appropriate query from query critera and filter queries and sets the field list.
     * Also the Solr query parser is chosen (lucene/standard).
     * @param criteria List of query strings that will be connected with AND
     * @param filters List of filter query strings
     * @param pageable
     * @return
     */
    @Override
    public Page<DataSheetDocument> generalSearch(List<String> criteria, List<String> filters, Pageable pageable) {
        SimpleQuery simpleQuery = new SimpleQuery();
        //Add Query criteria
        for (String c: criteria){
            simpleQuery.addCriteria(new SimpleStringCriteria(c));
        }
        //Add Filter Queries
        for (String filter: filters) {
            simpleQuery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(filter)));
        }
        //Further configuration
        simpleQuery.addProjectionOnField("*");
        simpleQuery.addProjectionOnField("[child parentFilter=docType:datasheet]");
        simpleQuery.setPageRequest(pageable).setDefType("lucene");
        return solrTemplate.queryForPage("dataSheet", simpleQuery, DataSheetDocument.class);
    }

    /**
     * Generates an appropriate query from query critera and filter queries and sets the field list.
     * Also the Solr query parser is chosen (lucene/standard).
     * @param queryString simple query string
     * @param filters List of filter query strings
     * @param pageable
     * @return
     */
    @Override
    public FacetPage<DataSheetDocument> advancedSearchFacet(String queryString, List<String> filters, Pageable pageable) {
        //TODO: Add Faceting functionality
        //Add Query
        SimpleQuery simpleQuery = new SimpleQuery(new SimpleStringCriteria(queryString));
        //Add Filter Queries
        for (String filter: filters) {
            simpleQuery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(filter)));
        }
        //Further configuration
        simpleQuery.addProjectionOnField("*");
        simpleQuery.addProjectionOnField("[child parentFilter=docType:datasheet]");
        simpleQuery.setPageRequest(pageable).setDefType("lucene");
        //return solrTemplate.queryForFacetPage("dataSheet", simpleQuery, DataSheetDocument.class);
        return null;
    }

    @Override
    public FacetPage<DataSheetDocument> generalSearchFacet(List<String> criteria, List<String> filters, Pageable pageable) {
        //Add Query
        FacetQuery simpleFacetQuery = new SimpleFacetQuery();
        for (String c: criteria){
            simpleFacetQuery.addCriteria(new SimpleStringCriteria(c));
        }
        //Add Filter Queries
        for (String filter: filters) {
            simpleFacetQuery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(filter)));
        }
        //Further configuration
        simpleFacetQuery.addProjectionOnField(new SimpleField("*"));
        simpleFacetQuery.addProjectionOnField(new SimpleField("[child parentFilter=docType:datasheet]"));
        simpleFacetQuery.setPageRequest(pageable).setDefType("lucene");

        FacetOptions facetOptions = new FacetOptions("fsc");
        facetOptions.setFacetLimit(100);
        facetOptions.addFacetOnField("niin");
        simpleFacetQuery.setFacetOptions(facetOptions);
        return solrTemplate.queryForFacetPage("dataSheet", simpleFacetQuery, DataSheetDocument.class);
    }
}
