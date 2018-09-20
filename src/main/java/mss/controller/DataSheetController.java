package mss.controller;

import mss.domain.entity.AdvancedTerm;
import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.Suggestions;
import mss.domain.responses.PageResponse;
import mss.service.DataSheetService;
import mss.service.SuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private SuggestService suggestService;

    @Autowired
    public DataSheetController(DataSheetService dataSheetService, SuggestService suggestService){
        this.dataSheetService = dataSheetService;
        this.suggestService = suggestService;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public PageResponse<DataSheetDocument> generalSearch(Pageable p, @RequestParam String s) {
        Page<DataSheetDocument> result = dataSheetService.generalSearch(p, s);
        return new PageResponse<>(result);
    }

    @RequestMapping(path = "/advancedSearch", method = RequestMethod.POST)
    public PageResponse<DataSheetDocument> advancedSearch(Pageable p, @RequestBody AdvancedTerm advancedTerm) {
        Page<DataSheetDocument> result = dataSheetService.advancedSearch(p, advancedTerm);
        return new PageResponse<>(result);
    }

    @RequestMapping(path = "/suggest", method = RequestMethod.GET)
    public Suggestions suggest(@RequestParam String s, @RequestParam String field, @RequestParam(required = false) Integer count) {
        Suggestions result = suggestService.createSuggestions(s, field, count);
        return result;
    }
}
