package com.am_cars.apuntes_mecanica.controller;

import com.am_cars.apuntes_mecanica.entity.Vehicle;
import com.am_cars.apuntes_mecanica.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Vehicle
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;
	
	/**
	 * Crea un nuevo vehículo - ADMIN y OWNER
	 */
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
	public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {
		try {
			Vehicle created = vehicleService.create(vehicle);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	/**
	 * Obtiene un vehículo por ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
		return vehicleService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Obtiene todos los vehículos
	 */
	@GetMapping
	public ResponseEntity<List<Vehicle>> getAll() {
		List<Vehicle> vehicles = vehicleService.findAll();
		return ResponseEntity.ok(vehicles);
	}
	
	/**
	 * Actualiza un vehículo existente - ADMIN y OWNER
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
	public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody Vehicle vehicle) {
		try {
			Vehicle updated = vehicleService.update(id, vehicle);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Elimina un vehículo por ID - Solo ADMIN
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			vehicleService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Busca un vehículo por placa
	 */
	@GetMapping("/plate/{plate}")
	public ResponseEntity<Vehicle> getByPlate(@PathVariable String plate) {
		return vehicleService.findByPlate(plate)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Verifica si existe un vehículo con la placa dada
	 */
	@GetMapping("/plate/{plate}/exists")
	public ResponseEntity<Boolean> existsByPlate(@PathVariable String plate) {
		boolean exists = vehicleService.existsByPlate(plate);
		return ResponseEntity.ok(exists);
	}
	
	/**
	 * Busca todos los vehículos de un propietario
	 */
	@GetMapping("/owner/{vehicleOwnerId}")
	public ResponseEntity<List<Vehicle>> getByVehicleOwnerId(@PathVariable Long vehicleOwnerId) {
		List<Vehicle> vehicles = vehicleService.findByVehicleOwnerId(vehicleOwnerId);
		return ResponseEntity.ok(vehicles);
	}
	
	/**
	 * Busca un vehículo por número de chasis
	 */
	@GetMapping("/chassis/{chassisNumber}")
	public ResponseEntity<Vehicle> getByChassisNumber(@PathVariable String chassisNumber) {
		return vehicleService.findByChassisNumber(chassisNumber)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}

