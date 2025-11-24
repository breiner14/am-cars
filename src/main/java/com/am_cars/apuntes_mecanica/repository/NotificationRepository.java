package com.am_cars.apuntes_mecanica.repository;

import com.am_cars.apuntes_mecanica.entity.Notification;
import com.am_cars.apuntes_mecanica.entity.Procedure;
import com.am_cars.apuntes_mecanica.entity.User;
import com.am_cars.apuntes_mecanica.entity.Vehicle;
import com.am_cars.apuntes_mecanica.entity.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Notification
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	/**
	 * Busca todas las notificaciones de un vehículo
	 */
	List<Notification> findByVehicle(Vehicle vehicle);
	
	/**
	 * Busca todas las notificaciones por ID del vehículo
	 */
	List<Notification> findByVehicleId(Long vehicleId);
	
	/**
	 * Busca todas las notificaciones de un procedimiento
	 */
	List<Notification> findByProcedure(Procedure procedure);
	
	/**
	 * Busca todas las notificaciones por ID del procedimiento
	 */
	List<Notification> findByProcedureCode(Integer procedureCode);
	
	/**
	 * Busca notificaciones por estado
	 */
	List<Notification> findByStatus(NotificationStatus status);
	
	/**
	 * Busca notificaciones por estado ordenadas por fecha de creación descendente
	 */
	List<Notification> findByStatusOrderByCreatedAtDesc(NotificationStatus status);
	
	/**
	 * Busca todas las notificaciones de un vehículo ordenadas por fecha de creación descendente
	 */
	List<Notification> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
	
	/**
	 * Busca todas las notificaciones creadas por un usuario
	 */
	List<Notification> findByCreatedBy(User createdBy);
	
	/**
	 * Busca todas las notificaciones creadas por un usuario (por ID)
	 */
	List<Notification> findByCreatedById(Long createdById);
	
	/**
	 * Busca todas las notificaciones creadas por un usuario ordenadas por fecha de creación descendente
	 */
	List<Notification> findByCreatedByIdOrderByCreatedAtDesc(Long createdById);
	
}

