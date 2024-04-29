package com.keep.changes.photo;

import com.keep.changes.fundraiser.FundraiserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhotoDto {

	private long id;

	private String photoUrl;

	private FundraiserDto fundraiserDto;

}