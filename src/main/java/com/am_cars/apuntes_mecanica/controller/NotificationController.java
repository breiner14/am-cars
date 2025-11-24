package com.am_cars.apuntes_mecanica.controller;

import com.am_cars.apuntes_mecanica.entity.Notification;
import com.am_cars.apuntes_mecanica.entity.enums.NotificationStatus;
import com.am_cars.apuntes_mecanica.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Notification
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;
	
	/**
	 * Crea una nueva notificación - ADMIN y MECHANIC
	 */
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
	public ResponseEntity<Notification> create(@RequestBody Notification notification) {
		try {
			Notification created = notificationService.create(notification);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	/**
	 * Obtiene una notificación por ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Notification> getById(@PathVariable Long id) {
		return notificationService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	/**
	 * Obtiene todas las notificaciones
	 */
	@GetMapping
	public ResponseEntity<List<Notification>> getAll() {
		List<Notification> notifications = notificationService.findAll();
		return ResponseEntity.ok(notifications);
	}
	
	/**
	 * Actualiza una notificación existente - ADMIN y MECHANIC
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC')")
	public ResponseEntity<Notification> update(@PathVariable Long id, @RequestBody Notification notification) {
		try {
			Notification updated = notificationService.update(id, notification);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Elimina una notificación por ID - Solo ADMIN
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			notificationService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Busca todas las notificaciones de un vehículo
	 */
	@GetMapping("/vehicle/{vehicleId}")
	public ResponseEntity<List<Notification>> getByVehicleId(@PathVariable Long vehicleId) {
		List<Notification> notifications = notificationService.findByVehicleId(vehicleId);
		return ResponseEntity.ok(notifications);
	}
	
	/**
	 * Busca todas las notificaciones de un vehículo ordenadas por fecha descendente
	 */
	@GetMapping("/vehicle/{vehicleId}/recent")
	public ResponseEntity<List<Notification>> getByVehicleIdOrderByCreatedAtDesc(@PathVariable Long vehicleId) {
		List<Notification> notifications = notificationService.findByVehicleIdOrderByCreatedAtDesc(vehicleId);
		return ResponseEntity.ok(notifications);
	}
	
	/**
	 * Busca todas las notificaciones de un procedimiento
	 */
	@GetMapping("/procedure/{procedureCode}")
	public ResponseEntity<List<Notification>> getByProcedureCode(@PathVariable Integer procedureCode) {
		List<Notification> notifications = notificationService.findByProcedureCode(procedureCode);
		return ResponseEntity.ok(notifications);
	}
	
	/**
	 * Busca notificaciones por estado
	 */
	@GetMapping("/status/{status}")
	public ResponseEntity<List<Notification>> getByStatus(@PathVariable NotificationStatus status) {
		List<Notification> notifications = notificationService.findByStatus(status);
		return ResponseEntity.ok(notifications);
	}
	
	/**
	 * Busca notificaciones por estado ordenadas por fecha descendente
	 */
	@GetMapping("/status/{status}/recent")
	public ResponseEntity<List<Notification>> getByStatusOrderByCreatedAtDesc(@PathVariable NotificationStatus status) {
		List<Notification> notifications = notificationService.findByStatusOrderByCreatedAtDesc(status);
		return ResponseEntity.ok(notifications);
	}
}

