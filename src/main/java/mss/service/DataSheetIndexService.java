package mss.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataSheetIndexService {

    private DataSheetRepository dataSheetRepository;

    private List<DataSheetDocument> documentCache;
    private Integer cacheLimit = 10000;

    @Autowired
    public DataSheetIndexService(DataSheetRepository dataSheetRepository){
        this.dataSheetRepository = dataSheetRepository;
        documentCache = new ArrayList<>();
    }

    public void add(DataSheetDocument document){
        addIdIfNecessary(document);
        dataSheetRepository.save(document);
    }

    public void addBulk(DataSheetDocument document){
        addIdIfNecessary(document);
        documentCache.add(document);
        if(documentCache.size()>cacheLimit){
            add(documentCache);
            documentCache = new ArrayList<>();
        }
    }

    public void add(List<DataSheetDocument> documents){
        documents.forEach(document -> addIdIfNecessary(document));
        dataSheetRepository.saveAll(documents);
    }

    public void remove(DataSheetDocument document){
        dataSheetRepository.delete(document);
    }

    public Boolean removeIfExists(DataSheetDocument document){
        if (document.hasId()){
            return removeIfExists(document.getId());
        }
        return false;
    }

    public Boolean removeIfExists(Long id){
        Boolean exists = dataSheetRepository.existsById(id);
        if (exists) dataSheetRepository.deleteById(id);
        return exists;
    }

    private Boolean addIdIfNecessary(DataSheetDocument document){
        if (!document.hasId()){
            document.setId(getNewDocumentId());
            return true;
        }
        return false;
    }

    private Long getNewDocumentId(){
        return dataSheetRepository.count() + documentCache.size();
    }

}
