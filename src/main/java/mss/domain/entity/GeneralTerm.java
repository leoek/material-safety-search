package mss.domain.entity;

public class GeneralTerm {
    private String searchTerm;
    private String fsgFacet;
    private String fscFacet;
    private Boolean fuzzy;
    private Boolean wholeDoc;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getFsgFacet() {
        return fsgFacet;
    }

    public void setFsgFacet(String fsgFacet) {
        this.fsgFacet = fsgFacet;
    }

    public String getFscFacet() {
        return fscFacet;
    }

    public void setFscFacet(String fscFacet) {
        this.fscFacet = fscFacet;
    }

    public Boolean getFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(Boolean fuzzy) {
        this.fuzzy = fuzzy;
    }

    public Boolean getWholeDoc() {
        return wholeDoc;
    }

    public void setWholeDoc(Boolean wholeDoc) {
        this.wholeDoc = wholeDoc;
    }
}
