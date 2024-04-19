package com.keep.changes.fundraiser;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keep.changes.account.AccountDto;
import com.keep.changes.address.AddressDto;
import com.keep.changes.category.CategoryDto;
import com.keep.changes.donation.DonationDto;
import com.keep.changes.pan.PanDto;
import com.keep.changes.photo.PhotoDto;
import com.keep.changes.user.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FundraiserDto {
	private long id;

	@NotEmpty
	private String fundraiserTitle;

	@NotEmpty
	private String fundraiserDescription;

	@NotEmpty
	private String cause;

	@NotEmpty
	private double raiseGoal;

	@JsonProperty(value = "raised", access = JsonProperty.Access.READ_ONLY)
	private double raised;

	@NotEmpty
	@Email(message = "Given email is not valid.")
	@Pattern(regexp = "^([a-zA-Z0-9._%-]{4,}+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Given email is not valid.")
	private String email;

//	@NotEmpty
	@Pattern(regexp = "(0|91)?[6-9][0-9]{9}", message = "Invalid Number.")
	private String phone;

	@JsonProperty(value = "startDate", access = JsonProperty.Access.READ_ONLY)
	private Date startDate;
	private Date endDate;

	@JsonProperty(value = "lastModifiedDate", access = JsonProperty.Access.READ_ONLY)
	private Date lastModifiedDate;

	@NotEmpty
	private String displayPhoto;

	private String coverPhoto;

	@JsonProperty(value = "isActive", access = JsonProperty.Access.READ_ONLY)
	private boolean isActive;

	@JsonProperty(value = "status", access = JsonProperty.Access.READ_ONLY)
	private AdminApproval approval;

	@JsonProperty(value = "status", access = JsonProperty.Access.READ_ONLY)
	private FundraiserStatus status;

	@NotEmpty
	private CategoryDto category;

	@JsonProperty(value = "postedBy", access = JsonProperty.Access.READ_ONLY)
	private UserDto postedBy;

	private Set<PhotoDto> photos;

	private AddressDto address;

	private PanDto pan;

	private AccountDto accounts;

	private Set<DonationDto> donationDtos;

}
