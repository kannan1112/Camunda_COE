package com.camundasaas.orchestration.Model.Client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Retailer {
    private String sno;
    private String retailerName;
    private String applicationStatus;
	@Override
	public String toString() {
		return "Retailer [sno=" + sno + ", retailerName=" + retailerName + ", applicationStatus=" + applicationStatus
				+ "]";
	}
    
    // getters and setters
}