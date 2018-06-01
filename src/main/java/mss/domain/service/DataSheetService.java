package mss.domain.service;

import mss.domain.entity.DataSheet;
import mss.domain.repository.DataSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSheetService {
    private final DataSheetRepository dataSheetRepository;

    @Autowired
    public DataSheetService(DataSheetRepository dataSheetRepository){
        this.dataSheetRepository = dataSheetRepository;
    }

    public void doStuff(){
        //this.dataSheetRepository.deleteAll();

        // insert some products
        DataSheet sheet = new DataSheet();
        sheet.setId(1L);
        sheet.setRaw("some pretty random text");
        this.dataSheetRepository.save(sheet);
    }
}
