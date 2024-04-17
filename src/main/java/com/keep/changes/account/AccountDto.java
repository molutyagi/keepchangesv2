package com.keep.changes.account;

import java.util.HashSet;
import java.util.Set;

import com.keep.changes.fundraiser.Fundraiser;
import com.keep.changes.user.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDto {

	private long id;

	@NotEmpty
	private String accountNumber;

	@NotEmpty
	private String idfc;

	@NotEmpty
	private String bankName;

	@NotEmpty
	private String branch;

	@NotEmpty
	private String holderName;

//	@NotEmpty
	private User holdingEntity;;

	private Set<Fundraiser> associatedFundraiser = new HashSet<>();

}