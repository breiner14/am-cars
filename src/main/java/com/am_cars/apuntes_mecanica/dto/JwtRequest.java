package com.am_cars.apuntes_mecanica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de autenticación (login)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
	
	private String username;
	private String password;
	
}

