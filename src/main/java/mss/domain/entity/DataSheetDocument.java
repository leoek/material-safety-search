package mss.domain.entity;


import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.solr.repository.Score;

import java.util.List;

@SolrDocument(solrCoreName = "dataSheet")
public class DataSheetDocument{


    @Id
    @Field
    private Long id;

    @Score
    private Double score;

    //Indexed and stored fields

    //@Indexed
    @Field
    private String productId;

    @Field
    private String fsc;

    @Field
    private String niin;

    @Field
    private String companyName;

    @Field
    private String msdsDate;

    @Field
    @ChildDocument
    List<IngredientDocument> ingredients;

    //Raw fields

    private String rawIdentification;

    private String rawComposition;

    private String rawHazards;

    private String rawFirstAid;

    private String rawFireFighting;

    private String rawAccidentalRelease;

    private String rawHandlingStorage;

    private String rawProtection;

    private String rawChemicalProperties;

    private String rawStabilityReactivity;

    private String rawDisposal;



    public Boolean hasId() {
        return id != null;
    }
}
