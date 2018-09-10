package mss.domain.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

public class IngredientDocument {

    @Id
    @Field
    private String id;

    @Field
    private String ingredName;

    @Field
    private String cas;

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
}
