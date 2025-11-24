package com.am_cars.apuntes_mecanica.service;

import com.am_cars.apuntes_mecanica.entity.Notification;
import com.am_cars.apuntes_mecanica.entity.Procedure;
import com.am_cars.apuntes_mecanica.entity.User;
import com.am_cars.apuntes_mecanica.entity.Vehicle;
import com.am_cars.apuntes_mecanica.entity.enums.NotificationStatus;
import com.am_cars.apuntes_mecanica.repository.NotificationRepository;
import com.am_cars.apuntes_mecanica.repository.ProcedureRepository;
import com.am_cars.apuntes_mecanica.repository.UserRepository;
import com.am_cars.apuntes_mecanica.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la entidad Notification
 */
@Service
@Transactional
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private ProcedureRepository procedureRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Crea una nueva notificación
	 */
	public Notification create(Notification notification) {
		// Verificar que el vehículo existe (obligatorio)
		if (notification.getVehicle() == null || notification.getVehicle().getId() == null) {
			throw new RuntimeException("El vehículo es obligatorio para crear una notificación");
		}
		Vehicle vehicle = vehicleRepository.findById(notification.getVehicle().getId())
				.orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + notification.getVehicle().getId()));
		notification.setVehicle(vehicle);
		
		// Verificar que el procedimiento existe (opcional)
		if (notification.getProcedure() != null && notification.getProcedure().getCode() != null) {
			Procedure procedure = procedureRepository.findByCode(notification.getProcedure().getCode())
					.orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con código: " + notification.getProcedure().getCode()));
			notification.setProcedure(procedure);
		}
		
		// Verificar que el usuario creador existe (obligatorio)
		if (notification.getCreatedBy() == null || notification.getCreatedBy().getId() == null) {
			throw new RuntimeException("El usuario creador es obligatorio para crear una notificación");
		}
		User createdBy = userRepository.findById(notification.getCreatedBy().getId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + notification.getCreatedBy().getId()));
		notification.setCreatedBy(createdBy);
		
		return notificationRepository.save(notification);
	}
	
	/**
	 * Obtiene una notificación por ID
	 */
	@Transactional(readOnly = true)
	public Optional<Notification> findById(Long id) {
		return notificationRepository.findById(id);
	}
	
	/**
	 * Obtiene todas las notificaciones
	 */
	@Transactional(readOnly = true)
	public List<Notification> findAll() {
		return notificationRepository.findAll();
	}
	
	/**
	 * Actualiza una notificación existente
	 */
	public Notification update(Long id, Notification notificationDetails) {
		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
		
		// Actualizar campos básicos
		if (notificationDetails.getMessage() != null) {
			notification.setMessage(notificationDetails.getMessage());
		}
		if (notificationDetails.getStatus() != null) {
			notification.setStatus(notificationDetails.getStatus());
		}
		if (notificationDetails.getNotificationDate() != null) {
			notification.setNotificationDate(notificationDetails.getNotificationDate());
		}
		
		// Actualizar vehículo si se proporciona
		if (notificationDetails.getVehicle() != null && notificationDetails.getVehicle().getId() != null) {
			Vehicle vehicle = vehicleRepository.findById(notificationDetails.getVehicle().getId())
					.orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + notificationDetails.getVehicle().getId()));
			notification.setVehicle(vehicle);
		}
		
		// Actualizar procedimiento si se proporciona (puede ser null para eliminar la relación)
		if (notificationDetails.getProcedure() != null && notificationDetails.getProcedure().getCode() != null) {
			Procedure procedure = procedureRepository.findByCode(notificationDetails.getProcedure().getCode())
					.orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con código: " + notificationDetails.getProcedure().getCode()));
			notification.setProcedure(procedure);
		} else if (notificationDetails.getProcedure() != null && notificationDetails.getProcedure().getCode() == null) {
			// Si se envía un objeto procedure vacío, eliminar la relación
			notification.setProcedure(null);
		}
		
		// Nota: El usuario creador (createdBy) no se actualiza, ya que es inmutable
		// Nota: La fecha de creación (createdAt) no se actualiza, ya que es inmutable
		
		return notificationRepository.save(notification);
	}
	
	/**
	 * Elimina una notificación por ID
	 */
	public void deleteById(Long id) {
		if (!notificationRepository.existsById(id)) {
			throw new RuntimeException("Notificación no encontrada con ID: " + id);
		}
		notificationRepository.deleteById(id);
	}
	
	/**
	 * Busca todas las notificaciones de un vehículo
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByVehicleId(Long vehicleId) {
		return notificationRepository.findByVehicleId(vehicleId);
	}
	
	/**
	 * Busca todas las notificaciones de un vehículo ordenadas por fecha descendente
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId) {
		return notificationRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId);
	}
	
	/**
	 * Busca todas las notificaciones de un procedimiento
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByProcedureCode(Integer procedureCode) {
		return notificationRepository.findByProcedureCode(procedureCode);
	}
	
	/**
	 * Busca notificaciones por estado
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByStatus(NotificationStatus status) {
		return notificationRepository.findByStatus(status);
	}
	
	/**
	 * Busca notificaciones por estado ordenadas por fecha descendente
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByStatusOrderByCreatedAtDesc(NotificationStatus status) {
		return notificationRepository.findByStatusOrderByCreatedAtDesc(status);
	}
	
	/**
	 * Busca todas las notificaciones creadas por un usuario
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByCreatedById(Long createdById) {
		return notificationRepository.findByCreatedById(createdById);
	}
	
	/**
	 * Busca todas las notificaciones creadas por un usuario ordenadas por fecha descendente
	 */
	@Transactional(readOnly = true)
	public List<Notification> findByCreatedByIdOrderByCreatedAtDesc(Long createdById) {
		return notificationRepository.findByCreatedByIdOrderByCreatedAtDesc(createdById);
	}
}

