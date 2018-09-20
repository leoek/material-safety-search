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

import java.util.*;
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

    public Suggestions createSuggestions(String s, String field) {
        Suggestions suggestions = new Suggestions();

        List<DataSheetDocument> suggestionsDocuments = dataSheetRepository.autocompleteList(s, field);
        List<String> suggestionsList;

        switch (field) {
            case "productId":
                suggestionsList = suggestionsDocuments.stream().map(doc -> doc.getProductId()).collect(Collectors.toList());
                break;
            case "fsc" :
                suggestionsList = suggestionsDocuments.stream().map(doc -> doc.getFsc()).collect(Collectors.toList());
                break;
            case "fscString" :
                suggestionsList = suggestionsDocuments.stream().map(doc -> doc.getFscString()).collect(Collectors.toList());
                break;
            case "fsgString":
                suggestionsList = suggestionsDocuments.stream().map(doc -> doc.getFsgString()).collect(Collectors.toList());
                break;
            case "niin" :
                suggestionsList = suggestionsDocuments.stream().map(doc -> doc.getNiin()).collect(Collectors.toList());
                break;
            case "companyName":
                suggestionsList = suggestionsDocuments.stream().map(doc -> doc.getCompanyName()).collect(Collectors.toList());
                break;
            default:
                log.error("Unsupported field type requested for suggestions");
                suggestionsList = Collections.<String>emptyList();
        }

        suggestions.setSuggestions(suggestionsList);
        return suggestions;
    }
}
