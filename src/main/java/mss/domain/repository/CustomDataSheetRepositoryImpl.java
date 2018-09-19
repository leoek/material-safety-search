package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;

import java.util.List;

public class CustomDataSheetRepositoryImpl implements CustomDataSheetRepository {
    //@Query(value = "?0", fields = {"*", "[child parentFilter=docType:datasheet]"}, defType = "lucene")
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

    @Override
    public Page<DataSheetDocument> generalSearch(List<String> criteria, List<String> filters, Pageable pageable) {
        SimpleQuery simpleQuery = new SimpleQuery();
        //Add Query criteria
        for (String c: criteria){
            simpleQuery.addCriteria(new SimpleStringCriteria(c));
        }
        //log.info(simpleQuery);
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

    @Override
    public Page<DataSheetDocument> advancedSearch(String queryString, List<String> filters, Pageable pageable) {
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
        return solrTemplate.queryForPage("dataSheet", simpleQuery, DataSheetDocument.class);
    }
}
