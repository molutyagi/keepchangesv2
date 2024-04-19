package com.keep.changes.fundraiser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keep.changes.payload.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/fundraisers")
public class FundraiserController {

	@Autowired
	private FundraiserService fundraiserService;

//	add fundraiser
	@PostMapping("add")
	public ResponseEntity<FundraiserDto> createFundraiser(@Valid @RequestBody FundraiserDto fundraiserDto) {

		FundraiserDto savedFundraiser = this.fundraiserService.createFundraiser(fundraiserDto);

		return new ResponseEntity<FundraiserDto>(savedFundraiser, HttpStatus.CREATED);
	}

//	Put Update 
	@PutMapping("fundraiser_{fId}")
	public ResponseEntity<FundraiserDto> putUpdateFundraiser(@PathVariable long fId,
			@Valid @RequestBody FundraiserDto fundraiserDto) {

		return ResponseEntity.ok(this.fundraiserService.putUpdateFundraiser(fId, fundraiserDto));
	}

//	Patch Update
	@PatchMapping("fundraiser_{fId}")
	public ResponseEntity<FundraiserDto> patchUpdateFundraiser(@Valid @PathVariable long fId,
			@RequestBody FundraiserDto partialFundraiserDto) {

		return ResponseEntity.ok(this.fundraiserService.patchFundraiser(fId, partialFundraiserDto));
	}

//	Delete
	@DeleteMapping("fundraiser_{fId}")
	public ResponseEntity<ApiResponse> deleteFundraiser(@PathVariable long fId) {
		this.fundraiserService.deleteFundraiser(fId);
		return ResponseEntity.ok(new ApiResponse("Fundraiser Deleted Successfully!!", true));
	}

//	Get
//	By Id
	@GetMapping("fundraiser_{fId}")
	public ResponseEntity<FundraiserDto> getById(@PathVariable long fId) {

		return ResponseEntity.ok(this.fundraiserService.getFundraiserById(fId));
	}

//	Get All
	@GetMapping(value = { "", "/" })
	public ResponseEntity<List<FundraiserDto>> getAll() {
		return ResponseEntity.ok(this.fundraiserService.getAllFundraisers());
	}

//	
}
