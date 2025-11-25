package com.am_cars.apuntes_mecanica.controller;

import com.am_cars.apuntes_mecanica.entity.VehicleOwner;
import com.am_cars.apuntes_mecanica.service.VehicleOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad VehicleOwner
 */
@RestController
@RequestMapping("/api/vehicle-owners")
public class VehicleOwnerController {
	
	@Autowired
	private VehicleOwnerService vehicleOwnerService;
	
	/**
	 * Crea un nuevo propietario de vehículo - Público (sin autenticación)
	 */
	@PostMapping
	public ResponseEntity<VehicleOwner> create(@RequestBody VehicleOwner vehicleOwner) {
		VehicleOwner created = vehicleOwnerService.create(vehicleOwner);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	/**
	 * Obtiene un propietario por ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<VehicleOwner> getById(@PathVariable Long id) {
		return vehicleOwnerService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Obtiene todos los propietarios
	 */
	@GetMapping
	public ResponseEntity<List<VehicleOwner>> getAll() {
		List<VehicleOwner> vehicleOwners = vehicleOwnerService.findAll();
		return ResponseEntity.ok(vehicleOwners);
	}
	
	/**
	 * Actualiza un propietario existente - Solo ADMIN
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VehicleOwner> update(@PathVariable Long id, @RequestBody VehicleOwner vehicleOwner) {
		try {
			VehicleOwner updated = vehicleOwnerService.update(id, vehicleOwner);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Elimina un propietario por ID - Solo ADMIN
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			vehicleOwnerService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Busca un propietario por username
	 */
	@GetMapping("/username/{username}")
	public ResponseEntity<VehicleOwner> getByUsername(@PathVariable String username) {
		return vehicleOwnerService.findByUsername(username)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Busca un propietario por email
	 */
	@GetMapping("/email/{email}")
	public ResponseEntity<VehicleOwner> getByEmail(@PathVariable String email) {
		return vehicleOwnerService.findByEmail(email)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Busca un propietario por número de documento
	 */
	@GetMapping("/documento/{numeroDoc}")
	public ResponseEntity<VehicleOwner> getByNumeroDoc(@PathVariable Integer numeroDoc) {
		return vehicleOwnerService.findByNumeroDoc(numeroDoc)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}

