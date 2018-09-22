package mss.service;

import mss.domain.entity.AdvancedTerm;
import mss.domain.entity.AdvancedTermIngredient;
import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.GeneralTerm;
import mss.domain.repository.DataSheetRepository;
import mss.domain.responses.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class containing all querying logic
 */
@Component
public class DataSheetService {

    private static final Logger log = LoggerFactory.getLogger(DataSheetService.class);

    private final DataSheetRepository dataSheetRepository;

    @Autowired
    public DataSheetService(DataSheetRepository dataSheetRepository) {
        this.dataSheetRepository = dataSheetRepository;
    }

    /**
     * Analyzes the given search term, turns it into SolrTemplate-digestable information and triggers a query for paged results.
     * Recognizes if a NIIN, a FSCG and multiple CAS numbers are entered and converts them into query terms that are mandatory for a match.
     * This allows users to use the General Search for exact document retrieval without needing to pay attention to detailed formatting.
     * Less strict than {@link #advancedSearch(Pageable, AdvancedTerm)} as recognized numbers are aggregated in a disjunctive term.
     *
     * @param p           Spring {@link Pageable} object
     * @param generalTerm Term object to be analyzed and turned into a query
     * @return Page of result documents
     */
    public PageResponse<DataSheetDocument> generalSearch(Pageable p, GeneralTerm generalTerm) {
        String searchTerm = generalTerm.getSearchTerm();
        if (searchTerm == null) searchTerm = "";
        log.info("Search term: \"" + searchTerm + "\"");

        //Regex
        AdvancedTerm advancedTerm = new AdvancedTerm();
        //NIIN (only first match)
        Pattern p1 = Pattern.compile("(!?[0-9]{2}[0-9a-zA-Z][0-9]{6}|[0-9]{2}-[0-9]{3}-[0-9]{4})");
        Matcher m1 = p1.matcher(searchTerm);
        if (m1.find()) {
            log.info("Found NIIN: " + m1.group(1));
            advancedTerm.setNiin(m1.group(1));

            searchTerm = searchTerm.replace(m1.group(1), " ");
        }

        Pattern p2 = Pattern.compile("(!?[0-9]{2,7}-[0-9]{2}-[0-9])");
        Matcher m2 = p2.matcher(searchTerm);
        List<AdvancedTermIngredient> ingredients = new ArrayList<>();
        while (m2.find()) {
            log.info("Found CAS: " + m2.group(1));
            AdvancedTermIngredient ati = new AdvancedTermIngredient();
            ati.setCas(m2.group(1));
            ingredients.add(ati);

            searchTerm = searchTerm.replace(m2.group(1), " ");
        }
        advancedTerm.setIngredients(ingredients);


        //FSC (only first match, because of building advancedTerm object)
        //Regex looks for common demarcations ( .,/) between Terms for higher accuracy
        Pattern p3 = Pattern.compile("(?:^|[ .,\\/]+)(!?[0-9]{4})(?:[ .,\\/]+|$)");
        Matcher m3 = p3.matcher(searchTerm);
        if (m3.find()) {
            log.info("Found FSC: " + m3.group(1));
            advancedTerm.setFscFacet(m3.group(1));

            searchTerm = searchTerm.replace(m3.group(1), " ");
        }

        //Create a query string from the populated AdvancedTerm object
        String advancedQueryString = advancedTermToQuery(advancedTerm);

        //Lists containing query information
        List<String> criteria = new ArrayList<>();
        List<String> filters = new ArrayList<>(advancedTermToFilterQueries(advancedTerm));

        if (!advancedQueryString.isEmpty()) {
            criteria.add(advancedQueryString);
        }

        searchTerm = searchTerm.trim().replaceAll("\\s{2,}", " ");
        if (!searchTerm.isEmpty()) {
            criteria.add("productId:(" + searchTerm + ") || " +
                    "companyName:(" + searchTerm + ") || " +
                    "fscString:(" + searchTerm + ") || " +
                    "fsgString:(" + searchTerm + ") || " +
                    "{!parent which=docType:datasheet v='ingredName:(" + searchTerm + ")'}");
        }

        //Edge case where only CAS numbers are entered.
        if (criteria.isEmpty()) {
            if (!filters.isEmpty()) {
                criteria.add("*:* && docType:datasheet");
            }
        }

        //Determine faceting & filter results if wished
        Boolean facetForFsc = false;
        if (generalTerm.getFsgFacet() != null) {
            if (generalTerm.getFsgFacet().matches("\\d{2}")) {
                //Now facet for FSCs instead
                facetForFsc = true;
                //And filter for FSG
                filters.add("fsg:" + generalTerm.getFsgFacet());
            } else {
                log.error("fsgFacet in supplied GeneralTerm object is not of length 2! Will ignore fsgFacet.");
            }
        }
        if (generalTerm.getFscFacet() != null) {
            if (generalTerm.getFscFacet().matches("\\d{4}")) {
                facetForFsc = true;
                //Add filter for FSC
                filters.add("fsc:" + generalTerm.getFscFacet());
            } else {
                log.error("fscFacet in supplied GeneralTerm object is not of length 4! Will ignore fscFacet.");
            }
        }

        log.info("Query criteria: " + criteria);
        log.info("Filter Queries: " + filters);

        if (criteria.isEmpty()) {
            criteria.add("*:* && docType:datasheet");
        }

        return dataSheetRepository.facetedSearch(criteria, filters, p, facetForFsc);
    }

    /**
     * Turns the given {@link AdvancedTerm} object into SolrTemplate-digestable information and triggers a query for paged results.
     * More strict than {@link #generalSearch(Pageable, GeneralTerm)} as it requires all specified search terms to be present in their respective fields.
     *
     * @param p            Spring {@link Pageable} object
     * @param advancedTerm {@link AdvancedTerm} object representing an advanced query term with multiple fields
     * @return Page of result documents
     */
    public PageResponse<DataSheetDocument> advancedSearch(Pageable p, AdvancedTerm advancedTerm) {
        //Generate Query
        String query = advancedTermToQuery(advancedTerm);
        if (query.isEmpty()) query = "*:* && docType:datasheet";

        //Generate Filters
        List<String> filters = advancedTermToFilterQueries(advancedTerm);

        //Determine faceting & filter results if wished
        Boolean facetForFsc = false;
        if (advancedTerm.getFsgFacet() != null) {
            if (advancedTerm.getFsgFacet().matches("\\d{2}")) {
                //Now facet for FSCs instead
                facetForFsc = true;
                //And filter for FSG
                filters.add("fsg:" + advancedTerm.getFsgFacet());
            } else {
                log.error("fsgFacet in supplied GeneralTerm object is not of length 2! Will ignore fsgFacet.");
            }
        }
        if (advancedTerm.getFscFacet() != null) {
            if (advancedTerm.getFscFacet().matches("\\d{4}")) {
                facetForFsc = true;
                //Add filter for FSC
                filters.add("fsc:" + advancedTerm.getFscFacet());
            } else {
                log.error("fscFacet in supplied GeneralTerm object is not of length 4! Will ignore fscFacet.");
            }
        }

        log.info("Query: " + query);
        log.info("Filter Queries: " + filters);

        List<String> queryAsList = new ArrayList<>();
        queryAsList.add(query);

        return dataSheetRepository.facetedSearch(queryAsList, filters, p, facetForFsc);
    }

    /**
     * Turns an {@link AdvancedTerm} object into a list of filter queries for filtering by ingredient information.
     *
     * @param advancedTerm {@link AdvancedTerm} object to extract filter queries from
     * @return List of filter queries as Strings. May be empty.
     */
    public List<String> advancedTermToFilterQueries(AdvancedTerm advancedTerm) {
        List<String> filters = new ArrayList<>();
        //Ingredients
        List<AdvancedTermIngredient> ingredients = advancedTerm.getIngredients();
        if (ingredients != null) {
            for (AdvancedTermIngredient ingredient : ingredients) {
                if (ingredient.getCas() != null && !ingredient.getCas().isEmpty()) {
                    String cas = ingredient.getCas();
                    if (cas.startsWith("!")) {
                        log.info("Negation of CAS numbers currently not supported.");
                        cas = cas.substring(1);
                    }
                    filters.add("{!parent which=docType:datasheet}cas:(" + cas + ")");
                }
                if (ingredient.getIngredName() != null && !ingredient.getIngredName().isEmpty()) {
                    String ingredName = ingredient.getIngredName();
                    if (ingredName.startsWith("!")) {
                        log.info("Negation of ingredient names currently not supported.");
                        ingredName = ingredName.substring(1);
                    }
                    filters.add("{!parent which=docType:datasheet}ingredName:(" + ingredName + ")");
                }
            }
        }

        return filters;
    }

    /**
     * Turns an {@link AdvancedTerm} object into a query string.
     *
     * @param advancedTerm {@link AdvancedTerm} object to extract information from
     * @return Query string
     */
    public String advancedTermToQuery(AdvancedTerm advancedTerm) {
        List<String> queryList = new ArrayList<>();

        String productId = advancedTerm.getProductId();
        if (productId != null && !productId.isEmpty()) {
            if (productId.startsWith("!")) {
                queryList.add("(!productId:(" + productId.substring(1) + ") && docType:datasheet)");
            } else {
                queryList.add("productId:(" + productId + ")");
            }
        }

        String fsc = advancedTerm.getFscFacet();
        if (fsc != null && !fsc.isEmpty()) {
            if (fsc.startsWith("!")) {
                queryList.add("(!fsc:(" + fsc.substring(1) + ") && docType:datasheet)");
            } else {
                queryList.add("fsc:(" + fsc + ")");
            }
        }

        String fscString = advancedTerm.getFscString();
        if (fscString != null && !fscString.isEmpty()) {
            if (fscString.startsWith("!")) {
                queryList.add("(!fscString:(" + fscString.substring(1) + ") && docType:datasheet)");
            } else {
                queryList.add("fscString:(" + fscString + ")");
            }
        }

        String fsgString = advancedTerm.getFsgString();
        if (fsgString != null && !fsgString.isEmpty()) {
            if (fsgString.startsWith("!")) {
                queryList.add("(!fsgString:(" + fsgString.substring(1) + ") && docType:datasheet)");
            } else {
                queryList.add("fsgString:(" + fsgString + ")");
            }
        }

        String niin = advancedTerm.getNiin();
        if (niin != null && !niin.isEmpty()) {
            if (niin.startsWith("!")) {
                queryList.add("(!niin:(" + niin.substring(1) + ") && docType:datasheet)");
            } else {
                queryList.add("niin:(" + niin + ")");
            }
        }

        String companyName = advancedTerm.getCompanyName();
        if (companyName != null && !companyName.isEmpty()) {
            if (companyName.startsWith("!")) {
                queryList.add("(!companyName:(" + companyName.substring(1) + ") && docType:datasheet)");
            } else {
                queryList.add("companyName:(" + companyName + ")");
            }
        }

        //Date filter
        Date beginDate = advancedTerm.getBeginDate();
        Date endDate = advancedTerm.getEndDate();
        if ((beginDate != null) && (endDate != null)) {
            if (beginDate.before(endDate) || beginDate.equals(endDate)) {
                queryList.add("msdsDate:[(" + beginDate.toInstant().toString() + ") TO (" + endDate.toInstant().toString() + ")]");
            } else {
                log.info("End date is before begin date. Ignoring dates.");
            }
        } else {
            log.info("Begin or end date is null. Ignoring dates.");
        }

        return queryList.stream().collect(Collectors.joining(" && "));
    }


}
