package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSheetRepository extends SolrCrudRepository<DataSheetDocument, Long>, CustomDataSheetRepository{
    @Query(value = "*:* && docType:datasheet", fields = {"*", "[child parentFilter=docType:datasheet]"})
    public Page<DataSheetDocument> getAllDocuments(Pageable pageable);
}
