package mss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.SimpleSolrResponse;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.UpdateField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for specifying schema details via the Solr configuration API
 */
@Service
public class SolrSetupService {

    private static final Logger log = LoggerFactory.getLogger(SolrSetupService.class);

    @Autowired
    private SolrClient dataSheetSolrClient;

    /**
     * Sets a partial schema with detailed field property information via the solr configuration API
     * @throws IOException Network error.
     * @throws SolrServerException Solr error.
     */
    public void setup() throws IOException, SolrServerException {
        SolrClient solrClient = dataSheetSolrClient;

        GenericSolrRequest request = new GenericSolrRequest(SolrRequest.METHOD.GET, "/schema/fields", null);
        SimpleSolrResponse response = request.process(solrClient);

        //Check if this schema is already imported
        if(!response.getResponse().toString().contains("productId")){

            String addModifiedFieldTypes = "{ \"add-field-type\": {\n" +
                    "            \"name\": \"text_en_splitting_mod\",\n" +
                    "            \"class\": \"solr.TextField\",\n" +
                    "            \"autoGeneratePhraseQueries\": \"true\",\n" +
                    "            \"positionIncrementGap\": \"100\",\n" +
                    "            \"indexAnalyzer\": {\n" +
                    "                \"tokenizer\": {\n" +
                    "                    \"class\": \"solr.WhitespaceTokenizerFactory\"\n" +
                    "                },\n" +
                    "                \"filters\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.StopFilterFactory\",\n" +
                    "                        \"words\": \"lang/stopwords_en.txt\",\n" +
                    "                        \"ignoreCase\": \"true\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.WordDelimiterGraphFilterFactory\",\n" +
                    "                        \"catenateNumbers\": \"1\",\n" +
                    "                        \"generateNumberParts\": \"1\",\n" +
                    "                        \"splitOnCaseChange\": \"1\",\n" +
                    "                        \"generateWordParts\": \"1\",\n" +
                    "                        \"catenateAll\": \"0\",\n" +
                    "                        \"catenateWords\": \"1\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.LowerCaseFilterFactory\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.KeywordMarkerFilterFactory\",\n" +
                    "                        \"protected\": \"protwords.txt\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.PorterStemFilterFactory\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.FlattenGraphFilterFactory\"\n" +
                    "                    },\n" +
                    "                    {\"class\": \"solr.EdgeNGramFilterFactory\",\n" +
                    "                    \"minGramSize\": \"2\",\n" +
                    "                    \"maxGramSize\": \"15\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            \"queryAnalyzer\": {\n" +
                    "                \"tokenizer\": {\n" +
                    "                    \"class\": \"solr.WhitespaceTokenizerFactory\"\n" +
                    "                },\n" +
                    "                \"filters\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.SynonymGraphFilterFactory\",\n" +
                    "                        \"expand\": \"true\",\n" +
                    "                        \"ignoreCase\": \"true\",\n" +
                    "                        \"synonyms\": \"synonyms.txt\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.StopFilterFactory\",\n" +
                    "                        \"words\": \"lang/stopwords_en.txt\",\n" +
                    "                        \"ignoreCase\": \"true\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.WordDelimiterGraphFilterFactory\",\n" +
                    "                        \"catenateNumbers\": \"0\",\n" +
                    "                        \"generateNumberParts\": \"1\",\n" +
                    "                        \"splitOnCaseChange\": \"1\",\n" +
                    "                        \"generateWordParts\": \"1\",\n" +
                    "                        \"catenateAll\": \"0\",\n" +
                    "                        \"catenateWords\": \"0\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.LowerCaseFilterFactory\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.KeywordMarkerFilterFactory\",\n" +
                    "                        \"protected\": \"protwords.txt\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.PorterStemFilterFactory\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        }, \"add-field-type\": " +
                    "           {\n" +
                    "            \"name\": \"string_mod\",\n" +
                    "            \"class\": \"solr.TextField\",\n" +
                    "            \"sortMissingLast\": true,\n" +
                    "            \"docValues\": true\n" +
                    "            \"indexAnalyzer\": {\n" +
                    "                \"tokenizer\": {\n" +
                    "                    \"class\": \"solr.WhitespaceTokenizerFactory\"\n" +
                    "                },\n" +
                    "                \"filters\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"solr.EdgeNGramFilterFactory\",\n" +
                    "                        \"minGramSize\": \"2\",\n" +
                    "                        \"maxGramSize\": \"15\"\n" +
                    "                    }" +
                    "                 ]\n" +
                    "              }" +
                    "            }" +
                    " }";



            GenericSolrRequest genericSolrModifyRequest = new GenericSolrRequest(SolrRequest.METHOD.POST, "/schema", null);
            ContentStream contentModifyStream = new ContentStreamBase.StringStream(addModifiedFieldTypes);
            genericSolrModifyRequest.setContentStreams(Collections.singleton(contentModifyStream));
            genericSolrModifyRequest.process(solrClient);

            String fieldTypeText = "text_en_splitting_mod";
            String fieldTypeRawText = "string_mod";

            //Create Schema
            //Maybe custom fieldTypes for productId, companyName and ingredName
            //Maybe set required to true for some fields
            List<ObjectNode> fieldArray = new ArrayList<>();
            //fieldArray.add(fieldObjectJson("id", "string", null, true, true, true, false, false));
            //fieldArray.add(fieldObjectJson("score"));
            //fieldArray.add(fieldObjectJson("docType"));
            fieldArray.add(fieldObjectJson("productId", fieldTypeText, true, true, false, false, false));
            fieldArray.add(fieldObjectJson("fsc", "string", true, true, false, false, false));
            fieldArray.add(fieldObjectJson("fscString", fieldTypeText, true, true, false, false, false));
            fieldArray.add(fieldObjectJson("fsgString", fieldTypeText, true, true, false, false, false));
            fieldArray.add(fieldObjectJson("niin", "string", true, true, false, false, false));
            fieldArray.add(fieldObjectJson("companyName", fieldTypeText,true, true, false, false, false));
            fieldArray.add(fieldObjectJson("msdsDate", "pdate", true, true, false, false, false));
            //fieldArray.add(fieldObjectJson("ingredients"));
            fieldArray.add(fieldObjectJson("ingredName", fieldTypeText,true, true, false, false, false));
            fieldArray.add(fieldObjectJson("cas", "string", true, true, false, false, false));

            fieldArray.add(fieldObjectJson("rawIdentification", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawComposition", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawHazards", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawFirstAid", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawFireFighting", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawAccidentalRelease", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawHandlingStorage", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawProtection", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawChemicalProperties", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawStabilityReactivity", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawDisposal", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawToxic", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawEco", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawTransport", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawRegulatory", fieldTypeRawText, false, true, false, false, false));
            fieldArray.add(fieldObjectJson("rawOther", fieldTypeRawText, false, true, false, false, false));

            String command = fieldArrayJson(fieldArray);

            GenericSolrRequest genericSolrRequest = new GenericSolrRequest(SolrRequest.METHOD.POST, "/schema", null);
            ContentStream contentStream = new ContentStreamBase.StringStream(command);
            genericSolrRequest.setContentStreams(Collections.singleton(contentStream));
            genericSolrRequest.process(solrClient);

            log.info("Partial schema imported. Remaining fields will be automatically added.");
        } else {
            log.info("Schema is already imported.");
        }
    }

    /**
     * Creates a JSON object node from the given input that represents one field configuration
     * @param name Corresponds to Solr's Field Property parameter "name"
     * @param type Corresponds to Solr's Field Property parameter "type"
     * @param indexed Corresponds to Solr's Field Property parameter "indexed"
     * @param stored Corresponds to Solr's Field Property parameter "stored"
     * @param required Corresponds to Solr's Field Property parameter "required"
     * @param docValues Corresponds to Solr's Field Property parameter "docValues"
     * @param multiValued Corresponds to Solr's Field Property parameter "multiValued"
     * @return The created field configuration object
     */
    private ObjectNode fieldObjectJson(String name, String type, Boolean indexed, Boolean stored, Boolean required, Boolean docValues, Boolean multiValued){
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

    /**
     * Turns multiple field configuration objects into a JSON String for adding fields via the Solr configuration API
     * @param fields List of field configuration objects
     * @return JSON string for Solr config API
     */
    private String fieldArrayJson(List<ObjectNode> fields){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode fieldArray = mapper.createArrayNode();
        ObjectNode finalObject = mapper.createObjectNode();
        for (ObjectNode field: fields) {
            fieldArray.add(field);
        }
        finalObject.putPOJO("add-field", fieldArray);

        return finalObject.toString();
    }
}
