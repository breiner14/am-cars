package com.am_cars.apuntes_mecanica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad VehicleOwner - Representa un propietario de vehículo
 * Hereda de User
 */
@Entity
@Table(name = "vehicle_owner")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class VehicleOwner extends User {
	
	/**
	 * Relación One-to-Many: Un VehicleOwner puede tener 0 o muchos vehículos
	 */
	@OneToMany(mappedBy = "vehicleOwner", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Vehicle> vehicles = new ArrayList<>();

	
}

