package mss.service;


import mss.domain.entity.DataSheetDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@Component
public class SuggestService {

    private static final Logger log = LoggerFactory.getLogger(DataSheetService.class);

    private DataSheetService dataSheetService;

    @Autowired
    public SuggestService(DataSheetService dataSheetService){
        this.dataSheetService = dataSheetService;
    }


    @ResponseBody
    @GetMapping
    @RequestMapping(path="/autocomplete", produces = "application/json")
    public Set<String> autocomplete(@RequestParam("term") String query) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptySet();
        }

        PageRequest pageRequest = PageRequest.of(0, 1);

        FacetPage<DataSheetDocument> result = (FacetPage<DataSheetDocument>) dataSheetService.findFullText(pageRequest, query);


        Set<String> suggestions = new LinkedHashSet<String>();
        for (Page<FacetFieldEntry> page : result.getFacetResultPages()) {
            log.info(page.toString());
            for (FacetFieldEntry entry : page) {
                log.info(entry.toString());
                if (entry.getValue().contains(query)) {
                    suggestions.add(entry.getValue());
                }
            }
        }

        return suggestions;
    }
}
