# Colección de Postman - Apuntes Mecánica API

Esta colección contiene todos los endpoints de la API de Apuntes Mecánica para facilitar las pruebas y el desarrollo.

## Autenticación JWT

La API utiliza autenticación basada en tokens JWT. Para usar los endpoints protegidos:

1. **Primero, realiza el login** usando el endpoint `POST /api/auth/login`
2. El token JWT se guarda automáticamente en la variable de colección `jwt_token`
3. Todos los endpoints protegidos incluyen automáticamente el header `Authorization: Bearer {{jwt_token}}`

### Roles disponibles:
- **ADMIN**: Acceso completo a todos los endpoints
- **OWNER**: Propietario de vehículo - puede gestionar sus vehículos
- **MECHANIC**: Mecánico - puede crear y actualizar procedimientos y notificaciones

## 📋 Contenido

La colección incluye los siguientes grupos de endpoints:

### 0. **Authentication** (Autenticación) - ⚠️ PÚBLICO
- Login - Autentica y obtiene token JWT
- Register Owner - Registra un nuevo propietario
- Register Mechanic - Registra un nuevo mecánico

### 1. **Mechanics** (Mecánicos) - 🔒 Requiere autenticación
- Crear mecánico
- Obtener todos los mecánicos
- Obtener mecánico por ID
- Actualizar mecánico
- Eliminar mecánico
- Buscar por username, email o documento

### 2. **Vehicle Owners** (Propietarios) - 🔒 Requiere autenticación
- Crear propietario
- Obtener todos los propietarios
- Obtener propietario por ID
- Actualizar propietario
- Eliminar propietario
- Buscar por username, email o documento

### 3. **Vehicles** (Vehículos) - 🔒 Requiere autenticación
- Crear vehículo
- Obtener todos los vehículos
- Obtener vehículo por ID
- Actualizar vehículo
- Eliminar vehículo
- Buscar por placa, chasis o propietario
- Verificar existencia de placa

### 4. **Procedures** (Procedimientos) - 🔒 Requiere autenticación
- Crear procedimiento
- Obtener todos los procedimientos
- Obtener procedimiento por ID o código
- Actualizar procedimiento
- Eliminar procedimiento
- Buscar por vehículo o nombre
- Verificar existencia de código

### 5. **Notifications** (Notificaciones) - 🔒 Requiere autenticación
- Crear notificación
- Obtener todas las notificaciones
- Obtener notificación por ID
- Actualizar notificación
- Eliminar notificación
- Buscar por vehículo, procedimiento o estado
- Obtener notificaciones recientes

## 🚀 Cómo usar

### Importar la colección en Postman

1. Abre Postman
2. Haz clic en **Import** (botón en la esquina superior izquierda)
3. Selecciona el archivo `Apuntes_Mecanica_API.postman_collection.json`
4. La colección se importará con todos los endpoints organizados

### Configurar la URL base

La colección usa una variable `base_url` que está configurada por defecto como:
```
http://localhost:8080/api
```

Para cambiar la URL base:
1. Haz clic en la colección "Apuntes Mecánica API"
2. Ve a la pestaña **Variables**
3. Modifica el valor de `base_url` según tu entorno:
   - Desarrollo: `http://localhost:8080/api`
   - Producción: `https://tu-dominio.com/api`

### Flujo de autenticación

1. **Registrar un usuario** (opcional, si no existe):
   - Usa `POST /api/auth/register/owner` o `POST /api/auth/register/mechanic`
   
2. **Hacer login**:
   - Ejecuta `POST /api/auth/login` con username y password
   - El token se guarda automáticamente en `jwt_token`
   
3. **Usar endpoints protegidos**:
   - Todos los endpoints protegidos ya incluyen el header `Authorization: Bearer {{jwt_token}}`
   - El token se renueva automáticamente al hacer login nuevamente

### Ejemplos de uso

#### Login
```json
POST {{base_url}}/auth/login
{
  "username": "juan_mechanic",
  "password": "password123"
}

Respuesta:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "juan_mechanic",
  "email": "juan@taller.com",
  "role": "MECHANIC"
}
```

#### Crear un Mecánico (Requiere ADMIN)
```json
POST {{base_url}}/mechanics
Headers: Authorization: Bearer {{jwt_token}}
{
  "role": "MECHANIC",
  "username": "juan_mechanic",
  "password": "password123",
  "email": "juan@taller.com",
  "tipoDocumento": "CC",
  "numeroDoc": 12345678,
  "telefono": 3001234567,
  "direccionTaller": "Calle 123 #45-67",
  "telefonoTaller": "6012345678",
  "nombreTaller": "Taller Juan",
  "horarioAtencion": "Lun-Vie 8:00-18:00",
  "especialidades": ["MECANICO", "MOTORES", "FRENOS"]
}
```

#### Crear un Vehículo (Requiere ADMIN u OWNER)
```json
POST {{base_url}}/vehicles
Headers: Authorization: Bearer {{jwt_token}}
{
  "plate": "ABC123",
  "cylinderCapacity": "1600cc",
  "color": "Rojo",
  "chassisNumber": "CH123456789",
  "model": 2020,
  "kilometraje": 50000,
  "vehicleOwner": {
    "id": 1
  }
}
```

#### Crear un Procedimiento (Requiere ADMIN o MECHANIC)
```json
POST {{base_url}}/procedures
Headers: Authorization: Bearer {{jwt_token}}
{
  "name": "Cambio de aceite",
  "duration": 60,
  "date": "2024-01-15",
  "note": 5,
  "vehicle": {
    "id": 1
  },
  "mechanic": {
    "id": 1
  }
}
```

## 📝 Notas importantes

### Permisos por Rol

- **ADMIN**: Acceso completo a todos los endpoints (crear, leer, actualizar, eliminar)
- **OWNER**: 
  - Puede crear y actualizar vehículos
  - Puede leer todos los recursos
  - No puede eliminar vehículos (solo ADMIN)
- **MECHANIC**:
  - Puede crear y actualizar procedimientos y notificaciones
  - Puede leer todos los recursos
  - No puede eliminar recursos (solo ADMIN)

### Especialidades de Mecánico
Los valores válidos para `especialidades` son:
- `MECANICO`
- `ELECTRICO`
- `MOTORES`
- `SUSPENSION`
- `TRANSMISION`
- `FRENOS`
- `CLIMATIZACION`
- `CARROCERIA`
- `PINTURA`
- `DIAGNOSTICO`

### Estados de Notificación
Los valores válidos para `status` en notificaciones son:
- `PENDING` - Pendiente
- `SENT` - Enviada
- `COMPLETED` - Completada

### Relaciones entre entidades

- **Vehicle** requiere un `vehicleOwner` (id del propietario)
- **Procedure** requiere un `vehicle` (id del vehículo) y un `mechanic` (id del mecánico)
- **Notification** requiere un `vehicle` (id del vehículo) y un `procedure` (código del procedimiento)

### Cambios importantes

- El campo `rol` (String) ha sido reemplazado por `role` (Enum: ADMIN, OWNER, MECHANIC)
- Todos los endpoints protegidos requieren el header `Authorization: Bearer <token>`
- El token JWT se guarda automáticamente al hacer login
- El token expira después de 24 horas (configurable)

## 🔧 Requisitos

- Postman instalado (versión 7.0 o superior)
- API ejecutándose en `http://localhost:8080` (o ajustar la variable `base_url`)

## 📚 Estructura de la API

Todos los endpoints siguen el patrón REST:
- `GET` - Obtener recursos
- `POST` - Crear recursos
- `PUT` - Actualizar recursos
- `DELETE` - Eliminar recursos

Los códigos de respuesta HTTP utilizados:
- `200 OK` - Operación exitosa
- `201 Created` - Recurso creado exitosamente
- `204 No Content` - Recurso eliminado exitosamente
- `400 Bad Request` - Solicitud inválida
- `404 Not Found` - Recurso no encontrado

