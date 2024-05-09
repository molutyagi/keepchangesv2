package com.keep.changes.email;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpDto {

	private String email;
	private String phone;
	private String name;

}
