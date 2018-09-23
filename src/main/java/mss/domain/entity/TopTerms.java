package mss.domain.entity;

import java.util.List;

public class TopTerms {
    private List<String> topTerms;

    public TopTerms(List<String> topTerms) {
        this.topTerms = topTerms;
    }

    public List<String> getTopTerms() {
        return topTerms;
    }

    public void setTopTerms(List<String> topTerms) {
        this.topTerms = topTerms;
    }
}
