package com.keep.changes.category;

import java.util.List;

public interface CategoryService {

//	add a category
	CategoryDto addCategory(CategoryDto categoryDto);

//	update
//	put update
	CategoryDto putUpdateCategory(Long cId, CategoryDto categoryDto);

//	patch update
	CategoryDto patchUpdateCategory(Long cId, CategoryDto partialCategoryDto);

//	delete
//	category
	void deleteCategory(Long cId);

//	svg
	void deleteSvg(Long cId);

//	get
//	get by id
	CategoryDto getById(Long cId);

//	get all
	List<CategoryDto> getAll();

}
