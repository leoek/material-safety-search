package mss.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.solr.repository.Score;

import java.util.Date;
import java.util.List;

@SolrDocument(solrCoreName = "dataSheet")
public class DataSheetDocument{


    @Id
    @Indexed
    private String id;

    @Score
    private Double score;

    //Enables Solr to recognize a "datasheet" as a parent of an "ingredient" in queries
    @Indexed
    private String docType;

    //Indexed and stored fields
    @Indexed
    private String productId;

    @Indexed
    private String fsc;

    @Indexed
    private String fscString;

    @Indexed
    @JsonIgnore
    private String fscFacet;

    @Indexed
    private String fsg;
    
    @Indexed
    private String fsgString;

    @Indexed
    @JsonIgnore
    private String fsgFacet;
    
    @Indexed
    private String niin;

    @Indexed
    private String companyName;

    @Indexed
    private Date msdsDate;

    @Indexed
    @ChildDocument
    List<IngredientDocument> ingredients;

    //Raw fields
    @Indexed(searchable = false)
    private String rawIdentification;

    @Indexed(searchable = false)
    private String rawComposition;

    @Indexed(searchable = false)
    private String rawHazards;

    @Indexed(searchable = false)
    private String rawFirstAid;

    @Indexed(searchable = false)
    private String rawFireFighting;

    @Indexed(searchable = false)
    private String rawAccidentalRelease;

    @Indexed(searchable = false)
    private String rawHandlingStorage;

    @Indexed(searchable = false)
    private String rawProtection;

    @Indexed(searchable = false)
    private String rawChemicalProperties;

    @Indexed(searchable = false)
    private String rawStabilityReactivity;

    @Indexed(searchable = false)
    private String rawDisposal;

    @Indexed(searchable = false)
    private String rawToxic;

    @Indexed(searchable = false)
    private String rawEco;

    @Indexed(searchable = false)
    private String rawTransport;

    @Indexed(searchable = false)
    private String rawRegulatory;

    @Indexed(searchable = false)
    private String rawOther;

    //Used by Importer

    public Boolean hasId() {
        return id != null;
    }

    //Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

    public String getFscString() {
		return fscString;
	}

	public void setFscString(String fscString) {
		this.fscString = fscString;
	}

    public String getFsg() {
        return fsg;
    }

    public void setFsg(String fsg) {
        this.fsg = fsg;
    }

	public String getFsgString() {
		return fsgString;
	}

	public void setFsgString(String fsgString) {
		this.fsgString = fsgString;
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

    public Date getMsdsDate() {
        return msdsDate;
    }

    public void setMsdsDate(Date msdsDate) {
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

    public String getRawToxic() {
        return rawToxic;
    }

    public void setRawToxic(String rawToxic) {
        this.rawToxic = rawToxic;
    }

    public String getRawEco() {
        return rawEco;
    }

    public void setRawEco(String rawEco) {
        this.rawEco = rawEco;
    }

    public String getRawTransport() {
        return rawTransport;
    }

    public void setRawTransport(String rawTransport) {
        this.rawTransport = rawTransport;
    }

    public String getRawRegulatory() {
        return rawRegulatory;
    }

    public void setRawRegulatory(String rawRegulatory) {
        this.rawRegulatory = rawRegulatory;
    }

    public String getRawOther() {
        return rawOther;
    }

    public void setRawOther(String rawOther) {
        this.rawOther = rawOther;
    }

    public String getFscFacet() {
        return fscFacet;
    }

    public void setFscFacet(String fscFacet) {
        this.fscFacet = fscFacet;
    }

    public String getFsgFacet() {
        return fsgFacet;
    }

    public void setFsgFacet(String fsgFacet) {
        this.fsgFacet = fsgFacet;
    }
}
