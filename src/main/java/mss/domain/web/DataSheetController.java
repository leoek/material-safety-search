package mss.domain.web;

import mss.domain.entity.DataSheetDocument;
import mss.domain.service.DataSheetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
@RequestMapping(path = "/ds")
public class DataSheetController {

    private DataSheetService dataSheetService;

    public DataSheetController(DataSheetService dataSheetService){
        this.dataSheetService = dataSheetService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<DataSheetDocument>> findFullText(Pageable p, @RequestParam(name = "s") String searchTerm) {
        Page<DataSheetDocument> result = dataSheetService.findFullText(p, searchTerm);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
