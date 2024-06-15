package com.keep.changes.fundraiser;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.keep.changes.account.AccountDto;
import com.keep.changes.address.AddressDto;
import com.keep.changes.category.CategoryDto;
import com.keep.changes.fundraiser.document.FundraiserDocumentDto;
import com.keep.changes.fundraiser.photo.PhotoDto;
import com.keep.changes.pan.PanDto;
import com.keep.changes.user.UserDto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
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
	private Long id;

	@NotEmpty
	private String fundraiserTitle;

	@NotEmpty
	private String fundraiserDescription;

	@NotEmpty
	private String beneficiary;

	@NonNull
	private Double raiseGoal;

	@JsonProperty(access = Access.READ_ONLY)
	private Double raised;

	@NotEmpty
	@Email(message = "Given email is not valid.")
	@Pattern(regexp = "^([a-zA-Z0-9._%-]{4,}+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Given email is not valid.")
	private String email;

	@NotEmpty
	@Pattern(regexp = "(0|91)?[6-9][0-9]{9}", message = "Invalid Number Format.")
	private String phone;

	@JsonProperty(access = Access.READ_ONLY)
	private Date startDate;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Future(message = "End date can only be in future")
	private Date endDate;

	@JsonProperty(access = Access.READ_ONLY)
	private Date lastModifiedDate;

//	@NotEmpty
	private String displayPhoto;

	private String coverPhoto;

	@JsonProperty(access = Access.READ_ONLY)
	private AdminApproval approval;

	@JsonProperty(access = Access.READ_ONLY)
	private String adminRemarks;

	private Boolean isReviewed;

	@JsonProperty(access = Access.READ_ONLY)
	private FundraiserStatus status;

	@JsonProperty(access = Access.READ_ONLY)
	private Boolean isActive;

	@NonNull
	private CategoryDto category;

	@JsonProperty(access = Access.READ_ONLY)
	private UserDto postedBy;

	private Set<PhotoDto> photos;

	private Set<FundraiserDocumentDto> documents;

	private AddressDto address;

	private PanDto pan;

	@JsonIgnore
	private AccountDto account;

	private Set<DonationDto> donations;

	@Override
	public String toString() {
		return "FundraiserDto [id=" + id + ", fundraiserTitle=" + fundraiserTitle + ", fundraiserDescription="
				+ fundraiserDescription + ", beneficiary=" + beneficiary + ", raiseGoal=" + raiseGoal + ", raised="
				+ raised + ", email=" + email + ", phone=" + phone + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", lastModifiedDate=" + lastModifiedDate + ", displayPhoto=" + displayPhoto + ", coverPhoto="
				+ coverPhoto + ", isActive=" + isActive + ", approval=" + approval + ",is Reviewed: " + isReviewed
				+ ", status=" + status + ", category=" + category + ", postedBy=" + postedBy + ", photos=" + photos
				+ ", address=" + address + ", pan=" + pan + ", account=" + account;
	}

}
