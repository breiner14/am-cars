package com.am_cars.apuntes_mecanica.controller;

import com.am_cars.apuntes_mecanica.dto.JwtRequest;
import com.am_cars.apuntes_mecanica.dto.JwtResponse;
import com.am_cars.apuntes_mecanica.entity.Mechanic;
import com.am_cars.apuntes_mecanica.entity.VehicleOwner;
import com.am_cars.apuntes_mecanica.entity.enums.Role;
import com.am_cars.apuntes_mecanica.repository.UserRepository;
import com.am_cars.apuntes_mecanica.service.AuthenticationService;
import com.am_cars.apuntes_mecanica.service.MechanicService;
import com.am_cars.apuntes_mecanica.service.VehicleOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VehicleOwnerService vehicleOwnerService;
	
	@Autowired
	private MechanicService mechanicService;
	
	/**
	 * Endpoint de login
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtRequest request) {
		try {
			JwtResponse response = authenticationService.authenticate(request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Error de autenticación: " + e.getMessage());
		}
	}
	
	/**
	 * Registro de propietario de vehículo
	 */
	@PostMapping("/register/owner")
	public ResponseEntity<?> registerOwner(@RequestBody VehicleOwner vehicleOwner) {
		try {
			// Verificar si el username o email ya existen
			if (userRepository.existsByUsername(vehicleOwner.getUsername())) {
				return ResponseEntity.badRequest().body("El username ya está en uso");
			}
			if (userRepository.existsByEmail(vehicleOwner.getEmail())) {
				return ResponseEntity.badRequest().body("El email ya está en uso");
			}
			if (userRepository.existsByNumeroDoc(vehicleOwner.getNumeroDoc())) {
				return ResponseEntity.badRequest().body("El número de documento ya está en uso");
			}
			
			// Establecer rol
			vehicleOwner.setRole(Role.OWNER);
			
			// Codificar contraseña
			String encodedPassword = authenticationService.encodePassword(vehicleOwner.getPassword());
			vehicleOwner.setPassword(encodedPassword);
			
			VehicleOwner created = vehicleOwnerService.create(vehicleOwner);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al registrar propietario: " + e.getMessage());
		}
	}
	
	/**
	 * Registro de mecánico
	 */
	@PostMapping("/register/mechanic")
	public ResponseEntity<?> registerMechanic(@RequestBody Mechanic mechanic) {
		try {
			// Verificar si el username o email ya existen
			if (userRepository.existsByUsername(mechanic.getUsername())) {
				return ResponseEntity.badRequest().body("El username ya está en uso");
			}
			if (userRepository.existsByEmail(mechanic.getEmail())) {
				return ResponseEntity.badRequest().body("El email ya está en uso");
			}
			if (userRepository.existsByNumeroDoc(mechanic.getNumeroDoc())) {
				return ResponseEntity.badRequest().body("El número de documento ya está en uso");
			}
			
			// Establecer rol
			mechanic.setRole(Role.MECHANIC);
			
			// Codificar contraseña
			String encodedPassword = authenticationService.encodePassword(mechanic.getPassword());
			mechanic.setPassword(encodedPassword);
			
			Mechanic created = mechanicService.create(mechanic);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al registrar mecánico: " + e.getMessage());
		}
	}
	
}

