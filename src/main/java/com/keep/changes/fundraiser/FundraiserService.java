package com.keep.changes.fundraiser;

import java.util.List;

import com.keep.changes.category.CategoryDto;
import com.keep.changes.user.UserDto;

public interface FundraiserService {

//	Add Fundraiser
	FundraiserDto createFundraiser(FundraiserDto fundraiserDto);

//	Put Update Fundraiser
	FundraiserDto putUpdateFundraiser(Long fId, FundraiserDto fundraiserDto);

//	Patch Update Fundraiser
	FundraiserDto patchFundraiser(Long fId, FundraiserDto fundraiserDto);

//	Delete Fundraiser
	void deleteFundraiser(Long fId);

//	Get Fundraiser
	FundraiserDto getFundraiserById(Long fId);

	List<FundraiserDto> getAllFundraisers();

	List<FundraiserDto> getFundraiserByEmail(String email);

	List<FundraiserDto> getFundraiserByPhone(String phone);

	List<FundraiserDto> getFundraisersByName(String name);

	List<FundraiserDto> getFundraisersByCategory(CategoryDto categoryDto);

	List<FundraiserDto> getFundraisersByPoster(UserDto userDto);

	List<FundraiserDto> getFundraisersByCause(String cause);

}