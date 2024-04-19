package com.keep.changes.fundraiser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminApproval {
	APPROVED("Approved"), PENDING("Pending"), DISAPPROVED("Cancelled");

	private final String approval;
}
