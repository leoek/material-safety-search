package mss.controller;

import mss.domain.entity.DataSheetDocument;
import mss.domain.responses.PageResponse;
import mss.service.DataSheetService;
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
@RequestMapping(path = "/search")
@CrossOrigin(origins = "*")
public class DataSheetController {

    private DataSheetService dataSheetService;

    public DataSheetController(DataSheetService dataSheetService){
        this.dataSheetService = dataSheetService;
    }

    @GetMapping
    public PageResponse<DataSheetDocument> findFullText(Pageable p, @RequestParam(name = "s") String searchTerm) {
        Page<DataSheetDocument> result = dataSheetService.findFullText(p, searchTerm);
        return new PageResponse<>(result);
    }
}
