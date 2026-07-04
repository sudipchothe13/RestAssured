package PojoLayer;

import java.util.List;

import lombok.Data;

@Data
public class Companies {

    private List<CompanyDetails> companies;

    public List<CompanyDetails> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDetails> companies) {
        this.companies = companies;
    }
}