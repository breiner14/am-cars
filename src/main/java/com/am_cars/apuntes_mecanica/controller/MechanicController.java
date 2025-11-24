package com.am_cars.apuntes_mecanica.controller;

import com.am_cars.apuntes_mecanica.entity.Mechanic;
import com.am_cars.apuntes_mecanica.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Mechanic
 */
@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {
	
	@Autowired
	private MechanicService mechanicService;
	
	/**
	 * Crea un nuevo mecánico - Solo ADMIN
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Mechanic> create(@RequestBody Mechanic mechanic) {
		Mechanic created = mechanicService.create(mechanic);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	/**
	 * Obtiene un mecánico por ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Mechanic> getById(@PathVariable Long id) {
		return mechanicService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Obtiene todos los mecánicos
	 */
	@GetMapping
	public ResponseEntity<List<Mechanic>> getAll() {
		List<Mechanic> mechanics = mechanicService.findAll();
		return ResponseEntity.ok(mechanics);
	}
	
	/**
	 * Actualiza un mecánico existente - Solo ADMIN
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Mechanic> update(@PathVariable Long id, @RequestBody Mechanic mechanic) {
		try {
			Mechanic updated = mechanicService.update(id, mechanic);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Elimina un mecánico por ID - Solo ADMIN
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			mechanicService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Busca un mecánico por username
	 */
	@GetMapping("/username/{username}")
	public ResponseEntity<Mechanic> getByUsername(@PathVariable String username) {
		return mechanicService.findByUsername(username)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Busca un mecánico por email
	 */
	@GetMapping("/email/{email}")
	public ResponseEntity<Mechanic> getByEmail(@PathVariable String email) {
		return mechanicService.findByEmail(email)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Busca un mecánico por número de documento
	 */
	@GetMapping("/documento/{numeroDoc}")
	public ResponseEntity<Mechanic> getByNumeroDoc(@PathVariable Integer numeroDoc) {
		return mechanicService.findByNumeroDoc(numeroDoc)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}

