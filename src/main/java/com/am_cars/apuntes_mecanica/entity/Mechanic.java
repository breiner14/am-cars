package com.am_cars.apuntes_mecanica.entity;

import com.am_cars.apuntes_mecanica.entity.enums.Speciality;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Mechanic - Representa un mecánico
 * Hereda de User
 */
@Entity
@Table(name = "mechanic")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class Mechanic extends User {
	
	@Column(name = "direccion_taller", length = 255)
	private String direccionTaller;
	
	@Column(name = "telefono_taller", length = 20)
	private Long telefonoTaller;
	
	@Column(name = "nombre_taller", length = 100)
	private String nombreTaller;
	
	@Column(name = "horario_atencion", length = 100)
	private String horarioAtencion;
	
	/**
	 * Especialidades del mecánico - Puede tener múltiples especialidades
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "mechanic_specialities", joinColumns = @JoinColumn(name = "mechanic_id"))
	@Column(name = "speciality")
	private List<Speciality> especialidades = new ArrayList<>();
	
}
