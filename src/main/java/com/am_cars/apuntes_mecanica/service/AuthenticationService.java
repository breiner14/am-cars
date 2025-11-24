package com.am_cars.apuntes_mecanica.service;

import com.am_cars.apuntes_mecanica.dto.JwtRequest;
import com.am_cars.apuntes_mecanica.dto.JwtResponse;
import com.am_cars.apuntes_mecanica.entity.User;
import com.am_cars.apuntes_mecanica.repository.UserRepository;
import com.am_cars.apuntes_mecanica.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio de autenticación
 */
@Service
@Transactional
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Autentica un usuario y genera un token JWT
	 */
	public JwtResponse authenticate(JwtRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getUsername(),
							request.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Credenciales inválidas", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		final User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		
		// Actualizar último login
		user.setLastLogin(LocalDateTime.now());
		userRepository.save(user);
		
		// Generar token con claims adicionales
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRole().name());
		claims.put("id", user.getId());
		claims.put("email", user.getEmail());
		
		final String token = jwtUtil.generateToken(userDetails, claims);
		
		return new JwtResponse(
				token,
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getRole().name()
		);
	}
	
	/**
	 * Codifica una contraseña
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
	
}

