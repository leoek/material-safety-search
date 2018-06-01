package mss.domain.repository;

import mss.domain.entity.DataSheet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.data.solr.repository.SolrRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface DataSheetRepository extends SolrCrudRepository<DataSheet, Long> {
}
