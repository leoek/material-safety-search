package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public interface DataSheetRepository extends SolrCrudRepository<DataSheetDocument, Long>, CustomDataSheetRepository{
    @Query(value = "*:* && docType:datasheet", fields = {"*", "[child parentFilter=docType:datasheet]"})
    public Page<DataSheetDocument> getAllDocuments(Pageable pageable);
}
