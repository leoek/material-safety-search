package mss.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Component;

@Component
public class DataSheetService {

    private static final Logger log = LoggerFactory.getLogger(DataSheetService.class);

    private final DataSheetRepository dataSheetRepository;

    @Autowired
    public DataSheetService(DataSheetRepository dataSheetRepository){
        this.dataSheetRepository = dataSheetRepository;
    }

    public Page<DataSheetDocument> findFullText(Pageable p, String searchTerm){
        Page<DataSheetDocument> result = dataSheetRepository.findAllDataSheetDocumentsWithIngredientDocuments(searchTerm, p);
        log.info(result.toString());
        return result;
    }
/*
    public FacetPage<DataSheetService> framentedFullText (Pageable p, String searchTerm) {
        FacetPage<DataSheetService> result = dataSheetRepository.findAll(Pageable p, searchTerm);
    }
    */
}
