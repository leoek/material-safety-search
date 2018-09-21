package mss.domain.entity;

import java.util.Date;
import java.util.List;

public class AdvancedTerm {
    private String productId;
    private String fscString;
    private String fsgString;
    private String niin;
    private String companyName;
    private Date beginDate;
    private Date endDate;
    private List<AdvancedTermIngredient> ingredients;
    public String fsgFacet;
    public String fscFacet;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFscString() {
        return fscString;
    }

    public void setFscString(String fscString) {
        this.fscString = fscString;
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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<AdvancedTermIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AdvancedTermIngredient> ingredients) {
        this.ingredients = ingredients;
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
}
