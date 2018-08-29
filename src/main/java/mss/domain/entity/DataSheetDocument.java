package mss.domain.entity;


import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.solr.repository.Score;

import java.util.List;

@SolrDocument(solrCoreName = "dataSheet")
public class DataSheetDocument {

    @Id
    @Field
    private Long id;

    @Field
    private String raw;

    @Field
    private List<String> lines;

    @Score
    private Double score;

    public Boolean hasId() {
        return id != null;
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
