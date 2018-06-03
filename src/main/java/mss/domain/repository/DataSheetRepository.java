package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSheetRepository extends SolrCrudRepository<DataSheetDocument, Long> {
}
