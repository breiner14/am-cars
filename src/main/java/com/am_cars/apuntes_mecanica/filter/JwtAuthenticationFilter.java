package com.am_cars.apuntes_mecanica.filter;

import com.am_cars.apuntes_mecanica.service.UserDetailsServiceImpl;
import com.am_cars.apuntes_mecanica.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para validar tokens JWT en cada solicitud
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		String requestPath = request.getRequestURI();
		
		// Saltar el filtro para endpoints públicos
		if (isPublicEndpoint(requestPath, request.getMethod())) {
			chain.doFilter(request, response);
			return;
		}
		
		final String authorizationHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwt = null;
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
				logger.error("Error al extraer username del token JWT", e);
			}
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				
				if (jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (UsernameNotFoundException e) {
				// Si el usuario no existe, simplemente continuar sin autenticación
				// No lanzar excepción para permitir que endpoints públicos funcionen
				logger.warn("Usuario no encontrado en el token JWT: " + username);
			} catch (Exception e) {
				logger.error("Error al validar token JWT", e);
			}
		}
		
		chain.doFilter(request, response);
	}
	
	/**
	 * Verifica si el endpoint es público (no requiere autenticación)
	 */
	private boolean isPublicEndpoint(String path, String method) {
		// Endpoints de autenticación
		if (path.startsWith("/api/auth/")) {
			return true;
		}
		
		// Endpoints de creación públicos
		if ("POST".equalsIgnoreCase(method)) {
			if (path.equals("/api/mechanics") || path.equals("/api/vehicle-owners")) {
				return true;
			}
		}
		
		return false;
	}
	
}

