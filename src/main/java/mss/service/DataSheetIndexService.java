package mss.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for adding {@link DataSheetDocument}s to the Solr index
 */
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

    /**
     * Adds a {@link DataSheetDocument} to a cache and indexes the whole cache when its size exceeds {@link #cacheLimit}
     * @param document {@link DataSheetDocument} to be added to the index
     */
    public void addBulk(DataSheetDocument document){
        documentCache.add(document);
        if(documentCache.size() >= cacheLimit){
            add(documentCache);
            documentCache = new ArrayList<>();
        }
    }

    /**
     * Flushes the cache. This should be called if the cache was used to index new documents and the last of
     * the new documents was added. If the cache wont be flushed, some documents will stay in the cache. These
     * documents would than never be indexed.
     */
    public void flushDocuemtCache(){
        add(documentCache);
        documentCache = new ArrayList<>();
    }

    /**
     * Adds the cached {@link DataSheetDocument} to the index
     * @param documents Cache of {@link DataSheetDocument} to add to the index
     */
    public void add(List<DataSheetDocument> documents){
        dataSheetRepository.saveAll(documents);
        log.info("Imported " + dataSheetRepository.count() + " documents, Cache #" + uploadCount++);
    }

    /**
     * Adds any remaining {@link DataSheetDocument} still in the cache
     */
    public void addRestStillInCache(){
        add(documentCache);
        documentCache = new ArrayList<>();
    }
}
