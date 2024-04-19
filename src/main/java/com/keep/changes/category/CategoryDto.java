package com.keep.changes.category;

import java.util.HashSet;
import java.util.Set;

import com.keep.changes.fundraiser.FundraiserDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

	private Long id;

	@NotEmpty
	private String categoryName;

	@NotEmpty
	private String categoryDescription;

//	@NotEmpty
	private String categorySvg;

	private Set<FundraiserDto> fundraisers = new HashSet<>();

	@Override
	public String toString() {
		return "CategoryDto [id=" + id + ", categoryName=" + categoryName + ", categoryDescription="
				+ categoryDescription + ", categorySvg=" + categorySvg + "]";
	}

}
