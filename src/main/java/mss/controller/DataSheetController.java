package mss.controller;

import mss.domain.entity.AdvancedTerm;
import mss.domain.entity.DataSheetDocument;
import mss.domain.responses.PageResponse;
import mss.service.DataSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * TODO wide open cors settings are ok for now.
 * Any client should be able to connect for now.
 */

@RestController
@Component
@CrossOrigin(origins = "*")
public class DataSheetController {

    private DataSheetService dataSheetService;

    @Autowired
    public DataSheetController(DataSheetService dataSheetService){
        this.dataSheetService = dataSheetService;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public PageResponse<DataSheetDocument> generalSearch(Pageable p, @RequestParam String searchTerm) {
        Page<DataSheetDocument> result = dataSheetService.generalSearch(p, searchTerm);
        return new PageResponse<>(result);
    }

    @RequestMapping(path = "/advancedSearch", method = RequestMethod.POST)
    public PageResponse<DataSheetDocument> advancedSearch(Pageable p, @RequestBody AdvancedTerm advancedTerm) {
        Page<DataSheetDocument> result = dataSheetService.advancedSearch(p, advancedTerm);
        return new PageResponse<>(result);
    }
}
