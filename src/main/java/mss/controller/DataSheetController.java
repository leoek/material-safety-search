package mss.controller;

import mss.domain.entity.DataSheetDocument;
import mss.domain.responses.PageResponse;
import mss.service.DataSheetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
@RequestMapping(path = "/search")
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
