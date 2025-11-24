package com.am_cars.apuntes_mecanica.entity;

import com.am_cars.apuntes_mecanica.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad base User - Representa un usuario en el sistema
 * 
 * Esta es una clase abstracta que será heredada por VehicleOwner y Mechanic
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private Role role;
	
	@Column(nullable = false, unique = true, length = 100)
	private String username;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false, unique = true, length = 255)
	private String email;
	
	@Column(name = "tipo_documento", nullable = false, length = 50)
	private String tipoDocumento;
	
	@Column(name = "numero_doc", nullable = false, unique = true)
	private Integer numeroDoc;
	
	@Column(length = 20)
	private Long telefono;
	
	@Column(name = "last_login")
	private LocalDateTime lastLogin;
	
}

