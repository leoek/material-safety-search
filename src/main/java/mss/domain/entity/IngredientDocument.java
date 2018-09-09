package mss.domain.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

public class IngredientDocument {

    @Id
    @Field
    private String id;

    private String ingredName;

    private String cas;
}
