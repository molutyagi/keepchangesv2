package com.keep.changes.pan;

import java.util.Set;

import com.keep.changes.fundraiser.FundraiserDto;
import com.keep.changes.user.UserDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PanDto {

	private long id;

	@NotEmpty
	private String panNumber;

	@NotEmpty
	private String nameOnPan;

	@NotEmpty
	private String panImage;

	private UserDto panHolderDto;

	private Set<FundraiserDto> fundraiserDtos;

}
