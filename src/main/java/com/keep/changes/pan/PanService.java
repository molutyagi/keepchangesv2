package com.keep.changes.pan;

import java.util.List;

public interface PanService {

//	Post
	PanDto addPan(PanDto panDto);

//	Update
//	Put Update
	PanDto putUpdatePan(PanDto panDto, Long pId);

//	Patch Update
	PanDto patchUpdatePan(PanDto partialPanDto, Long pId);

//	Delete
	void deletePan(Long pId);

//	Get
//	Get All
	List<PanDto> getAll();

//	Get by Id
	PanDto getById(Long pId);

//	Get by UserId
	PanDto getByPanHolder(Long uId);

//	Get by Pan Number
	PanDto getByPanNumber(String panNumber);

//	Get by name on pan
	List<PanDto> getByNameOnPan(String name);
}
