package com.keep.changes.fundraiser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.keep.changes.category.CategoryDto;
import com.keep.changes.exception.ApiException;
import com.keep.changes.exception.ResourceNotFoundException;
import com.keep.changes.user.UserDto;

import jakarta.transaction.Transactional;

@Service
public class FundraiserServiceImpl implements FundraiserService {

	@Autowired
	private FundraiserRepository fundraiserRepository;

	@Autowired
	private ModelMapper modelMapper;

//	create
	@Override
	@Transactional
	public FundraiserDto createFundraiser(FundraiserDto fundraiserDto) {

		Fundraiser fundraiser = this.modelMapper.map(fundraiserDto, Fundraiser.class);

		Fundraiser saved = this.fundraiserRepository.save(fundraiser);

		return this.modelMapper.map(saved, FundraiserDto.class);
	}

//	put update
	@Override
	@Transactional
	public FundraiserDto putUpdateFundraiser(Long fId, FundraiserDto fd) {
		Fundraiser fundraiser = this.fundraiserRepository.findById(fId)
				.orElseThrow(() -> new ResourceNotFoundException("Fundraiser", "Id", fId));

		fundraiser.putUpdateFundraiser(fId, fd.getFundraiserTitle(), fd.getFundraiserDescription(), fd.getCause(),
				fd.getRaiseGoal(), fd.getEmail(), fd.getPhone(), fd.getEndDate(), fd.getDisplayPhoto(),
				fd.getCoverPhoto());

		this.fundraiserRepository.save(fundraiser);

		return this.modelMapper.map(fundraiser, FundraiserDto.class);
	}

//	patch update
	@Override
	@Transactional
	public FundraiserDto patchFundraiser(Long fId, FundraiserDto partialFundraiserDto) {

		Fundraiser fundraiser = this.fundraiserRepository.findById(fId)
				.orElseThrow(() -> new ResourceNotFoundException("Fundraiser", "Id", fId));

		Fundraiser partialFundraiser = this.modelMapper.map(partialFundraiserDto, Fundraiser.class);

		Field[] declaredFields = Fundraiser.class.getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				Object value = field.get(partialFundraiser);
				if (value != null) {

					field.set(fundraiser, value);
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new ApiException("error updating fundraiser", HttpStatus.BAD_REQUEST, false);
			}
		}
		this.fundraiserRepository.save(fundraiser);
		return this.modelMapper.map(fundraiser, FundraiserDto.class);
	}

//	delete
	@Override
	@Transactional
	public void deleteFundraiser(Long fId) {
		Fundraiser fundraiser = this.fundraiserRepository.findById(fId)
				.orElseThrow(() -> new ResourceNotFoundException("Fundraiser", "Id", fId));

		this.fundraiserRepository.delete(fundraiser);
	}

//	get
//	by id
	@Override
	@Transactional
	public FundraiserDto getFundraiserById(Long fId) {
		Fundraiser fundraiser = this.fundraiserRepository.findById(fId)
				.orElseThrow(() -> new ResourceNotFoundException("Fundraiser", "Id", fId));
		return this.modelMapper.map(fundraiser, FundraiserDto.class);
	}

//	get all
	@Override
	@Transactional
	public List<FundraiserDto> getAllFundraisers() {
		List<Fundraiser> fundraisers = this.fundraiserRepository.findAll();

		return fundraiserToDto(fundraisers);

	}

//	by email
	@Override
	@Transactional
	public List<FundraiserDto> getFundraiserByEmail(String email) {
		List<Fundraiser> fundraisers = this.fundraiserRepository.findByEmail(email);

		return fundraiserToDto(fundraisers);
	}

//	by phone
	@Override
	@Transactional
	public List<FundraiserDto> getFundraiserByPhone(String phone) {
		List<Fundraiser> fundraisers = this.fundraiserRepository.findByPhone(phone);

		return fundraiserToDto(fundraisers);
	}

//	by title containing
	@Override
	@Transactional
	public List<FundraiserDto> getFundraisersByName(String name) {
		List<Fundraiser> fundraisers = this.fundraiserRepository.findByFundraiserTitleContaining(name);

		return fundraiserToDto(fundraisers);
	}

//	by category
	@Override
	@Transactional
	public List<FundraiserDto> getFundraisersByCategory(CategoryDto categoryDto) {

		List<Fundraiser> fundraisers = this.fundraiserRepository.findByCategory(categoryDto);

		return fundraiserToDto(fundraisers);
	}

//	by poster
	@Override
	@Transactional
	public List<FundraiserDto> getFundraisersByPoster(UserDto userDto) {

		List<Fundraiser> fundraisers = this.fundraiserRepository.findByPostedBy(userDto);

		return fundraiserToDto(fundraisers);
	}

//	by cause
	@Override
	@Transactional
	public List<FundraiserDto> getFundraisersByCause(String cause) {

		List<Fundraiser> fundraisers = this.fundraiserRepository.findByCauseContaining(cause);

		return fundraiserToDto(fundraisers);
	}

	private List<FundraiserDto> fundraiserToDto(List<Fundraiser> fundraisers) {

		List<FundraiserDto> fundraiserDtos = new ArrayList<FundraiserDto>();
		for (Fundraiser fundraiser : fundraisers) {
			fundraiserDtos.add(this.modelMapper.map(fundraiser, FundraiserDto.class));
		}

		return fundraiserDtos;
	}

}
