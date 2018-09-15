package mss.domain.repository;

import mss.domain.entity.DataSheetDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSheetRepository extends SolrCrudRepository<DataSheetDocument, Long> {

    @Query("productId:*?0*")
    public Page<DataSheetDocument> findFullText(String searchTerm, Pageable pageable);

/*    value is the query term used by solr
    fields are the fields to be returned.
    1.) "*" means return everything (the whole DataSheetDocument with all its fields)
    2.) "[child parentFilter=docType_s:datasheet]" returns children of each DataSheetDocument (i.e. list of its ingredients)
     and inserts them into the DataSheetDocument*/
    @Query(value = "productId:?0", fields = {"*", "[child parentFilter=docType:datasheet]"})
    public Page<DataSheetDocument> findAllDataSheetDocumentsWithIngredientDocuments(String searchTerm, Pageable pageable);
}
