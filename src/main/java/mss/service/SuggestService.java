package mss.service;


import mss.domain.entity.DataSheetDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class SuggestService {

    @ResponseBody
    @RequestMapping(value="/autocomplete", produces = "application/json")
    public Set<String> autocomplete(Model model, @RequestParam("term") String query) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptySet();
        }

        PageRequest pageRequest = PageRequest.of(0, 1);
        FacetPage<DataSheetDocument> result = DataSheetService.findFullText(pageRequest, query);

        Set<String> suggestions = new LinkedHashSet<String>();
        for (Page<FacetFieldEntry> page : result.getFacetResultPages()) {
            for (FacetFieldEntry entry : page) {
                if (entry.getValue().contains(query)) {
                    suggestions.add(entry.getValue());
                }
            }
        }

        return suggestions;
    }
}
