package com.am_cars.apuntes_mecanica.entity;

import com.am_cars.apuntes_mecanica.entity.enums.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * Entidad Notification - Representa una notificación
 * 
 * Relación: Una notificación está asociada a un Vehicle, opcionalmente a un Procedure,
 * y siempre a un User creador
 */
@Entity
@Table(name = "notification")
@Data
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notificacion_id")
	private Long notificacionId;
	
	/**
	 * Relación Many-to-One: Muchas notificaciones pertenecen a un Vehicle
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehiculo_id", nullable = false)
	@JsonIgnoreProperties({"procedures", "vehicleOwner", "hibernateLazyInitializer", "handler"})
	private Vehicle vehicle;
	
	/**
	 * Relación Many-to-One: Muchas notificaciones pueden pertenecer a un Procedure (opcional)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "procedure_id", nullable = true)
	@JsonIgnoreProperties({"vehicle", "mechanic", "hibernateLazyInitializer", "handler"})
	private Procedure procedure;
	
	/**
	 * Relación Many-to-One: Muchas notificaciones son creadas por un User
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_user_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User createdBy;
	
	@Column(length = 500)
	private String message;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private NotificationStatus status;
	
	/**
	 * Fecha de creación de la notificación en el sistema
	 */
	@Column(name = "created_at", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	/**
	 * Fecha en que se debe o se envió la notificación
	 */
	@Column(name = "notification_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date notificationDate;
	
	/**
	 * Pre-persist: Establece la fecha de creación automáticamente
	 */
	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = new Date();
		}
	}
	
}

