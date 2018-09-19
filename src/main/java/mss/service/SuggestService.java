package mss.service;


import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.Suggestions;
import mss.domain.repository.DataSheetRepository;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Component
public class SuggestService {

    private static final Logger log = LoggerFactory.getLogger(DataSheetService.class);

    private DataSheetRepository dataSheetRepository;

    @Autowired
    public SuggestService(DataSheetRepository dataSheetRepository){
        this.dataSheetRepository = dataSheetRepository;
    }

    public Suggestions createSuggestions(String s) {
        Suggestions suggestions = new Suggestions();

        List<DataSheetDocument> suggestionsDocuments = dataSheetRepository.product(s);

        List<String> suggestionsList = suggestionsDocuments.stream().collect(Collectors.mapping(doc -> doc.getProductId(), Collectors.toList()));

        suggestions.setSuggestions(suggestionsList);


        return suggestions;
    }
}
