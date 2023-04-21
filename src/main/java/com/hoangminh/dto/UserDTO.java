package com.hoangminh.dto;

import com.hoangminh.enumtype.USER_ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private String username;
	
	private String ho_ten;
	
	private String sdt;
	
	private String gioi_tinh;
	
	private String email;
	
	private String dia_chi;

	private USER_ROLE role;
}
