package mss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.catalina.LifecycleState;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SolrSetupService {

    private static final Logger log = LoggerFactory.getLogger(SolrSetupService.class);

    /**
     * @throws IOException network error.
     * @throws SolrServerException solr error.
     */
    public void setup() throws IOException, SolrServerException {

        String solrUrl = "http://localhost:8983/solr/dataSheet";
        SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();

        //Create Schema
        //Maybe custom fieldTypes for productId, companyName and ingredName
        //Maybe set required to true for some fields
        List<ObjectNode> fieldArray = new ArrayList<>();
        //fieldArray.add(fieldObjectJson("id", "string", null, true, true, true, false, false));
        //fieldArray.add(fieldObjectJson("score"));
        //fieldArray.add(fieldObjectJson("docType"));
        fieldArray.add(fieldObjectJson("productId", "text_general", true, true, false, false, false));
        fieldArray.add(fieldObjectJson("fsc", "string", true, true, false, false, false));
        fieldArray.add(fieldObjectJson("niin", "string", true, true, false, false, false));
        fieldArray.add(fieldObjectJson("companyName", "text_general",true, true, false, false, false));
        fieldArray.add(fieldObjectJson("msdsDate", "pdates", true, true, false, false, false));
        //fieldArray.add(fieldObjectJson("ingredients"));
        fieldArray.add(fieldObjectJson("ingredName", "text_general",true, true, false, false, false));
        fieldArray.add(fieldObjectJson("cas", "string", true, true, false, false, false));

        fieldArray.add(fieldObjectJson("rawIdentification", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawComposition", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawHazards", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawFirstAid", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawFireFighting", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawAccidentalRelease", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawHandlingStorage", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawProtection", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawChemicalProperties", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawStabilityReactivity", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawDisposal", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawToxic", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawEco", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawTransport", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawRegulatory", "text_general", false, true, false, false, false));
        fieldArray.add(fieldObjectJson("rawOther", "text_general", false, true, false, false, false));



        String command = fieldArrayJson(fieldArray);

        //log.info(command);

        GenericSolrRequest genericSolrRequest = new GenericSolrRequest(SolrRequest.METHOD.POST, "/schema", null);
        ContentStream contentStream = new ContentStreamBase.StringStream(command);
        genericSolrRequest.setContentStreams(Collections.singleton(contentStream));
        log.info(command);
        genericSolrRequest.process(solrClient);

        log.info("Partial schema imported.");

    }

    public ObjectNode fieldObjectJson(String name, String type, Boolean indexed, Boolean stored, Boolean required, Boolean docValues, Boolean multiValued){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode field = mapper.createObjectNode();

        field.put("name", name);
        field.put("type", type);
        field.put("indexed", indexed);
        field.put("stored", stored);
        field.put("required", required);
        field.put("docValues", docValues);
        field.put("multiValued", multiValued);

        return field;
    }

    public String fieldArrayJson(List<ObjectNode> fields){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode fieldArray = mapper.createArrayNode();
        ObjectNode finalObject = mapper.createObjectNode();

        for (ObjectNode field: fields) {
            fieldArray.add(field);
        }

        finalObject.putPOJO("add-field", fieldArray);

        return finalObject.toString();
    }

    public String fieldTypeJson(){
        return "";
    }


    public void examples() {
        //Pageable pageable = PageRequest.of(0, 10);

        //Page<DataSheetDocument> documents = dataSheetRepository.findFullText("SEMI GLOSS INTERIOR LATEX WHITE", pageable);

        //log.info("{}", documents);

        String urlString = "http://localhost:8983/solr/dataSheet";
        SolrClient solr = new HttpSolrClient.Builder(urlString).build();

        SolrQuery query = new SolrQuery();
        query.setQuery("lines:oxygen");
        query.setStart(0);

        try {
            QueryResponse response = solr.query(query);
            SolrDocumentList results = response.getResults();
            log.info("{}", results);
        } catch (SolrServerException | IOException solrE){
            log.info("{}", solrE);
        }
    }
}
