package PojoLayer;

import java.util.List;
import lombok.Data;

@Data
public class RootResponse {

	private DataResponse data;
	private Object meta;
	private List<CompanyDetails> companies;
	
	
	/*

	public DataResponse getData() {
		return data;
	}

	public void setData(DataResponse data) {
		this.data = data;
	}

	public Object getMeta() {
		return meta;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}
	
    

    public List<CompanyDetails> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDetails> companies) {
        this.companies = companies;
    }
    
*/
	
}
