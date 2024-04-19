package com.keep.changes.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keep.changes.role.RoleDto;

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
public class UserDto {
	@JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotEmpty
	private String name;

	@NotEmpty
	@Email(message = "Given email is not valid.")
	@Pattern(regexp = "^([a-zA-Z0-9._%-]{4,}+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Given email is not valid.")
	private String email;

	@NotEmpty
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,32}$", message = "Password must be between 8 - 32 characters long and must contain at least one letter, one number, and one special character")
	@JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
	private String password;

//	@NotEmpty
	@Pattern(regexp = "(0|91)?[6-9][0-9]{9}", message = "Invalid Number.")
	private String phone;

	private String displayImage;

	private String coverImage;

	private String about;

	@JsonProperty(value = "registerTime", access = JsonProperty.Access.READ_ONLY)
	private Date registerTime;

	@JsonProperty(value = "lastUpdateTime", access = JsonProperty.Access.READ_ONLY)
	private Date lastUpdateTime;

	private Set<RoleDto> roles = new HashSet<>();

//	private AddressDto address;

//	private PanDto pan;

//	private Set<AccountDto> accounts = new HashSet<>();

//	private Set<FundraiserDto> fundraisers = new HashSet<>();

//	private Set<DonationDto> donationDtos = new HashSet<>();

}
