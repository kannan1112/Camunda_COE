package com.camundasaas.orchestration.Model.Client;

public class VendorRegistration {

	private String companyName;
	private String vendorId;
	private String eMail;
	private String phone;
	private String tin;
	private String vendorAddress;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTin() {
		return tin;
	}
	public void setTin(String tin) {
		this.tin = tin;
	}
	public String getVendorAddress() {
		return vendorAddress;
	}
	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	@Override
	public String toString() {
		return "VendorRegistration [companyName=" + companyName + ", vendorId=" + vendorId + ", eMail=" + eMail
				+ ", phone=" + phone + ", tin=" + tin + ", vendorAddress=" + vendorAddress + "]";
	}

	

}
