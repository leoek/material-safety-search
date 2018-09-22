package mss.controller;

import mss.domain.entity.AdvancedTerm;
import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.GeneralTerm;
import mss.domain.entity.Suggestions;
import mss.domain.responses.PageResponse;
import mss.service.DataSheetService;
import mss.service.SuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
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

    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public PageResponse<DataSheetDocument> generalSearch(Pageable p, @RequestBody GeneralTerm generalTerm) {
        //Assert.notEmpty(generalTerm.getSearchTerm());
        return dataSheetService.generalSearch(p, generalTerm);
    }

    @RequestMapping(path = "/advancedSearch", method = RequestMethod.POST)
    public PageResponse<DataSheetDocument> advancedSearch(Pageable p, @RequestBody AdvancedTerm advancedTerm) {
        return dataSheetService.advancedSearch(p, advancedTerm);
    }

    @RequestMapping(path = "/suggest", method = RequestMethod.GET)
    public Suggestions suggest(@RequestParam String s, @RequestParam String field, @RequestParam(required = false) Integer count) {
        Suggestions result = suggestService.createSuggestions(s, field, count);
        return result;
    }
}
