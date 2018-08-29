package mss.domain.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DataSheetService {
    private final DataSheetRepository dataSheetRepository;

    @Autowired
    public DataSheetService(DataSheetRepository dataSheetRepository){
        this.dataSheetRepository = dataSheetRepository;
    }

    public Page<DataSheetDocument> findFullText(Pageable p, String searchTerm){
        Page<DataSheetDocument> result = dataSheetRepository.findFullText(searchTerm, p);
        return result;
    }
}
