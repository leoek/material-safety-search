package mss.controller;

import mss.domain.entity.AdvancedTerm;
import mss.domain.entity.DataSheetDocument;
import mss.domain.responses.PageResponse;
import mss.service.DataSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for exposing certain methods to the frontend
 * TODO: Wide open CORS settings are adequate for now. During development client should be able to connect for now.
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
    public PageResponse<DataSheetDocument> generalSearch(Pageable p, @RequestParam String s) {
        FacetPage<DataSheetDocument> result = dataSheetService.generalSearch(p, s);
        return new PageResponse<>(result);
    }

    /*@RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<Page<DataSheetDocument>> generalSearch(Pageable p, @RequestParam String s) {
        FacetPage<DataSheetDocument> result = dataSheetService.generalSearch(p, s);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }*/

    /*@RequestMapping(path = "/advancedSearch", method = RequestMethod.POST)
    public PageResponse<DataSheetDocument> advancedSearch(Pageable p, @RequestBody AdvancedTerm advancedTerm) {
        Page<DataSheetDocument> result = dataSheetService.advancedSearch(p, advancedTerm);
        return new PageResponse<>(result);
    }*/
}
