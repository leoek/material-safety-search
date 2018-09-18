package mss.domain.entity;

import java.util.Date;
import java.util.List;

public class AdvancedTerm {
    private String productId;
    private String fsc;
    private String niin;
    private String companyName;
    private Date beginDate;
    private Date endDate;
    private List<AdvancedTermIngredient> ingredients;

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
}
