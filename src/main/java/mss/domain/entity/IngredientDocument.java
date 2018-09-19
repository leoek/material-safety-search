package mss.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "dataSheet")
public class IngredientDocument {

    @Id
    @Indexed
    private String id;

    @Indexed
    private String docType;

    //Indexed and stored fields
    @Indexed
    private String ingredName;

    @Indexed
    private String cas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getIngredName() {
        return ingredName;
    }

    public void setIngredName(String ingredName) {
        this.ingredName = ingredName;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }
}
