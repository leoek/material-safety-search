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

    //Used by Importer

    public Boolean hasId() {
        return id != null;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFsc() {
        return fsc;
    }

    public void setFsc(String fsc) {
        this.fsc = fsc;
    }

    public String getNiin() {
        return niin;
    }

    public void setNiin(String niin) {
        this.niin = niin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMsdsDate() {
        return msdsDate;
    }

    public void setMsdsDate(String msdsDate) {
        this.msdsDate = msdsDate;
    }

    public List<IngredientDocument> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDocument> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRawIdentification() {
        return rawIdentification;
    }

    public void setRawIdentification(String rawIdentification) {
        this.rawIdentification = rawIdentification;
    }

    public String getRawComposition() {
        return rawComposition;
    }

    public void setRawComposition(String rawComposition) {
        this.rawComposition = rawComposition;
    }

    public String getRawHazards() {
        return rawHazards;
    }

    public void setRawHazards(String rawHazards) {
        this.rawHazards = rawHazards;
    }

    public String getRawFirstAid() {
        return rawFirstAid;
    }

    public void setRawFirstAid(String rawFirstAid) {
        this.rawFirstAid = rawFirstAid;
    }

    public String getRawFireFighting() {
        return rawFireFighting;
    }

    public void setRawFireFighting(String rawFireFighting) {
        this.rawFireFighting = rawFireFighting;
    }

    public String getRawAccidentalRelease() {
        return rawAccidentalRelease;
    }

    public void setRawAccidentalRelease(String rawAccidentalRelease) {
        this.rawAccidentalRelease = rawAccidentalRelease;
    }

    public String getRawHandlingStorage() {
        return rawHandlingStorage;
    }

    public void setRawHandlingStorage(String rawHandlingStorage) {
        this.rawHandlingStorage = rawHandlingStorage;
    }

    public String getRawProtection() {
        return rawProtection;
    }

    public void setRawProtection(String rawProtection) {
        this.rawProtection = rawProtection;
    }

    public String getRawChemicalProperties() {
        return rawChemicalProperties;
    }

    public void setRawChemicalProperties(String rawChemicalProperties) {
        this.rawChemicalProperties = rawChemicalProperties;
    }

    public String getRawStabilityReactivity() {
        return rawStabilityReactivity;
    }

    public void setRawStabilityReactivity(String rawStabilityReactivity) {
        this.rawStabilityReactivity = rawStabilityReactivity;
    }

    public String getRawDisposal() {
        return rawDisposal;
    }

    public void setRawDisposal(String rawDisposal) {
        this.rawDisposal = rawDisposal;
    }
}
