package mss.domain.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

public class IngredientDocument {

    @Id
    @Field
    private String id;

    private String ingredName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    private String cas;
}
