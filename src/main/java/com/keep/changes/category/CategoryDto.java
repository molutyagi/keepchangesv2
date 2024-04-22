package com.keep.changes.category;

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

	@NotEmpty
	private String categorySvg;

	@Override
	public String toString() {
		return "CategoryDto [id=" + id + ", categoryName=" + categoryName + ", categoryDescription="
				+ categoryDescription + ", categorySvg=" + categorySvg + "]";
	}

}
