package mss.domain.web;

import mss.domain.service.DataSheetService;
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
    public ResponseEntity<HttpStatus> doStuff(@RequestParam(name = "ids", required = false) List<Long> ids) {
        dataSheetService.doStuff();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
