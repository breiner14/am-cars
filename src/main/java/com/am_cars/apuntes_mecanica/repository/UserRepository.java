package com.am_cars.apuntes_mecanica.repository;

import com.am_cars.apuntes_mecanica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para la entidad User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * Busca un usuario por username
	 */
	Optional<User> findByUsername(String username);
	
	/**
	 * Busca un usuario por email
	 */
	Optional<User> findByEmail(String email);
	
	/**
	 * Busca un usuario por número de documento
	 */
	Optional<User> findByNumeroDoc(Integer numeroDoc);
	
	/**
	 * Verifica si existe un usuario con el username dado
	 */
	boolean existsByUsername(String username);
	
	/**
	 * Verifica si existe un usuario con el email dado
	 */
	boolean existsByEmail(String email);
	
	/**
	 * Verifica si existe un usuario con el número de documento dado
	 */
	boolean existsByNumeroDoc(Integer numeroDoc);
	
}

