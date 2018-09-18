package mss.domain.entity;

public class AdvancedTermIngredient {

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

    private String ingredName;
    private String cas;
}
