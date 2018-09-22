package mss.domain.responses;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SingleFacetResponse {
    private String type;
    private String facetString;
    private String facetNumber;
    private Long count;

    @JsonCreator
    public SingleFacetResponse() {
    }

    public SingleFacetResponse(String type, String facetInfo, Long count) {
        this.type = type;
        System.out.println(facetInfo);
        this.facetString = "No Facet String Found";
        this.facetNumber = "No Facet Number Found";
        if (facetInfo != null) {
            this.facetString = facetInfo.substring(0, facetInfo.indexOf("_"));
            this.facetNumber = facetInfo.substring(facetInfo.indexOf("_") + 1, facetInfo.length());
        }

        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFacetString() {
        return facetString;
    }

    public void setFacetString(String facetString) {
        this.facetString = facetString;
    }

    public String getFacetNumber() {
        return facetNumber;
    }

    public void setFacetNumber(String facetNumber) {
        this.facetNumber = facetNumber;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
