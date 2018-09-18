package mss.service;

import mss.domain.entity.AdvancedTerm;
import mss.domain.entity.AdvancedTermIngredient;
import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataSheetService {

    private static final Logger log = LoggerFactory.getLogger(DataSheetService.class);

    private final DataSheetRepository dataSheetRepository;

    @Autowired
    public DataSheetService(DataSheetRepository dataSheetRepository) {
        this.dataSheetRepository = dataSheetRepository;
    }

    public Page<DataSheetDocument> findFullText(Pageable p, String searchTerm) {
        Page<DataSheetDocument> result = dataSheetRepository.findAllDataSheetDocumentsWithIngredientDocuments(searchTerm, p);
        log.info(result.toString());
        return result;
    }

    public Page<DataSheetDocument> generalSearch(Pageable p, String searchTerm) {
        //Regex
        //NIIN

        //if multiple numbers etc. build Advanced object
        //Build Query
        //Search
        return null;
    }

    public Page<DataSheetDocument> advancedSearch(Pageable p, AdvancedTerm advancedTerm) {
        List<String> queryList = new ArrayList<>();

        String productId = advancedTerm.getProductId();
        if (productId != null) {
            if (productId.startsWith("!")) {
                queryList.add("!productId:\"" + productId.substring(1) + "\"");
            } else {
                queryList.add("productId:\"" + productId + "\"");
            }
        }

        String fsc = advancedTerm.getFsc();
        if (fsc != null) {
            if (fsc.startsWith("!")) {
                queryList.add("!fsc:\"" + fsc.substring(1) + "\"");
            } else {
                queryList.add("fsc:\"" + fsc + "\"");
            }
        }

        String fscString = advancedTerm.getFscString();
        if (fscString != null) {
            if (fscString.startsWith("!")) {
                queryList.add("!fscString:\"" + fscString.substring(1) + "\"");
            } else {
                queryList.add("fscString:\"" + fscString + "\"");
            }
        }

        String fsgString = advancedTerm.getFsgString();
        if (fsgString != null) {
            if (fsgString.startsWith("!")) {
                queryList.add("!fsgString:\"" + fsgString.substring(1) + "\"");
            } else {
                queryList.add("fsgString:\"" + fsgString + "\"");
            }
        }

        String niin = advancedTerm.getNiin();
        if (niin != null) {
            if (niin.startsWith("!")) {
                queryList.add("!niin:\"" + niin.substring(1) + "\"");
            } else {
                queryList.add("niin:\"" + niin + "\"");
            }
        }

        String companyName = advancedTerm.getCompanyName();
        if (companyName != null) {
            if (companyName.startsWith("!")) {
                queryList.add("!companyName:\"" + companyName.substring(1) + "\"");
            } else {
                queryList.add("companyName:\"" + companyName + "\"");
            }
        }

        //Date filter
        Date beginDate = advancedTerm.getBeginDate();
        Date endDate = advancedTerm.getEndDate();
        if ((beginDate != null) && (endDate != null)) {
            if (beginDate.before(endDate) || beginDate.equals(endDate)) {
                queryList.add("msdsDate:[\"" + beginDate.toInstant().toString() + "\" TO \"" + endDate.toInstant().toString() + "\"]");
            } else {
                log.info("End date is before begin date. Ignoring dates.");
            }
        } else {
            log.info("Begin or end date is null. Ignoring dates.");
        }

        //Ingredients
        List<AdvancedTermIngredient> ingredients = advancedTerm.getIngredients();
        if (ingredients != null) {
            for (AdvancedTermIngredient ingredient : ingredients) {
                if (ingredient.getCas() != null) {
                    queryList.add("({!parent which=docType:datasheet}cas:" + ingredient.getCas() + ")");
                }
                if (ingredient.getIngredName() != null) {
                    queryList.add("({!parent which=docType:datasheet}ingredName:" + ingredient.getIngredName() + ")");
                }
            }
        }

        String query = queryList.stream().collect(Collectors.joining(" && "));

        log.info("Query: " + query);

        List<String> filters = new ArrayList<>();
        return dataSheetRepository.advancedSearch(query, filters, p);
    }


}
