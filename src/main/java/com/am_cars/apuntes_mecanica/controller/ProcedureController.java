package com.am_cars.apuntes_mecanica.controller;

import com.am_cars.apuntes_mecanica.entity.Procedure;
import com.am_cars.apuntes_mecanica.service.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Procedure
 */
@RestController
@RequestMapping("/api/procedures")
public class ProcedureController {
	
	@Autowired
	private ProcedureService procedureService;
	
	/**
	 * Crea un nuevo procedimiento - ADMIN y MECHANIC
	 */
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
	public ResponseEntity<Procedure> create(@RequestBody Procedure procedure) {
		try {
			Procedure created = procedureService.create(procedure);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	/**
	 * Obtiene un procedimiento por código
	 */
	@GetMapping("/code/{code}")
	public ResponseEntity<Procedure> getByCode(@PathVariable Integer code) {
		return procedureService.findByCode(code)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Obtiene un procedimiento por ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Procedure> getById(@PathVariable Integer id) {
		return procedureService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Obtiene todos los procedimientos
	 */
	@GetMapping
	public ResponseEntity<List<Procedure>> getAll() {
		List<Procedure> procedures = procedureService.findAll();
		return ResponseEntity.ok(procedures);
	}
	
	/**
	 * Actualiza un procedimiento existente - ADMIN y MECHANIC
	 */
	@PutMapping("/code/{code}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
	public ResponseEntity<Procedure> update(@PathVariable Integer code, @RequestBody Procedure procedure) {
		try {
			Procedure updated = procedureService.update(code, procedure);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Elimina un procedimiento por código - Solo ADMIN
	 */
	@DeleteMapping("/code/{code}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Integer code) {
		try {
			procedureService.deleteByCode(code);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Busca todos los procedimientos de un vehículo
	 */
	@GetMapping("/vehicle/{vehicleId}")
	public ResponseEntity<List<Procedure>> getByVehicleId(@PathVariable Long vehicleId) {
		List<Procedure> procedures = procedureService.findByVehicleId(vehicleId);
		return ResponseEntity.ok(procedures);
	}
	
	/**
	 * Busca procedimientos por nombre (búsqueda parcial)
	 */
	@GetMapping("/search")
	public ResponseEntity<List<Procedure>> searchByName(@RequestParam String name) {
		List<Procedure> procedures = procedureService.findByNameContaining(name);
		return ResponseEntity.ok(procedures);
	}
	
	/**
	 * Verifica si existe un procedimiento con el código dado
	 */
	@GetMapping("/code/{code}/exists")
	public ResponseEntity<Boolean> existsByCode(@PathVariable Integer code) {
		boolean exists = procedureService.existsByCode(code);
		return ResponseEntity.ok(exists);
	}
}

