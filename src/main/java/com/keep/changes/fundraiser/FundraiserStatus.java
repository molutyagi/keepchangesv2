package com.keep.changes.fundraiser;
 
public enum FundraiserStatus {
	ACTIVE("Active"), COMPLETED("Completed"), CANCELLED("Cancelled");

	private final String status;

	FundraiserStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
