package mss.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataSheetIndexService {


    private static final Logger log = LoggerFactory.getLogger(DataSheetIndexService.class);
    private DataSheetRepository dataSheetRepository;

    private List<DataSheetDocument> documentCache;
    private Integer cacheLimit = 10000;
    private Integer uploadCount = 1;

    @Autowired
    public DataSheetIndexService(DataSheetRepository dataSheetRepository){
        this.dataSheetRepository = dataSheetRepository;
        documentCache = new ArrayList<>();
    }

    public void addBulk(DataSheetDocument document){
        documentCache.add(document);
        if(documentCache.size()>cacheLimit){
            add(documentCache);
            documentCache = new ArrayList<>();
        }
    }

    public void add(List<DataSheetDocument> documents){
        dataSheetRepository.saveAll(documents);
        log.info("Imported " + dataSheetRepository.count() + " documents, cache #" + uploadCount++);
    }
}
