# EvaluacionParcial3FullStack

Arquitectura de microservicios para la gestión de una **tienda online**: administra usuarios, catálogo de productos, inventario, carritos de compra, pedidos, pagos, envíos, notificaciones y reportes de venta. Desarrollado como Evaluación Parcial 3 de la asignatura **Desarrollo FullStack I (DSY1103)**.

## Integrantes

- Andrés Marín
- Diego Bergeret
- Thomas Araya

## Arquitectura

El sistema está compuesto por **12 servicios**: 10 microservicios de dominio, un servidor de descubrimiento (Eureka) y un API Gateway que centraliza el enrutamiento.

```
                    ┌──────────────────┐
   Cliente  ───────▶│   API Gateway    │  (puerto 8080)
 (Postman/Web)       └────────┬─────────┘
                               │  lb://...  (resuelve vía Eureka)
                               ▼
        ┌──────────────────────────────────────────┐
        │              Eureka Server                │ (puerto 8761)
        │      Service Registry / Discovery          │
        └──────────────────────────────────────────┘
                               ▲
        ┌──────────────────────┴──────────────────────┐
        │   10 microservicios de dominio (Spring Boot)  │
        │   Usuario · Categoria · Producto · Inventario │
        │   Carrito · Pedido · Pagos · Envio             │
        │   Notificacion · Reporte                       │
        └────────────────────────────────────────────────┘
```

Cada microservicio sigue el patrón **CSR (Controller–Service–Repository/Model)**, expone su propia documentación Swagger/OpenAPI, se registra en Eureka y se comunica con otros servicios mediante `WebClient`.

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje / runtime | Java 21 |
| Framework | Spring Boot 4.0.6 (microservicios de negocio) / 4.1.0 (Eureka y Gateway) |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway (variante MVC, `gateway-server-webmvc`) |
| Persistencia | Spring Data JPA + MySQL |
| Migraciones de BD | Flyway |
| Documentación de API | springdoc-openapi / Swagger UI |
| Comunicación entre servicios | Spring WebClient |
| Hypermedia | Spring HATEOAS (implementado en Producto y Usuario) |
| Testing | JUnit 5 + Mockito |
| Contenedores | Docker / Docker Compose |
| Base de datos en desarrollo | MySQL vía Laragon (host) |

## Microservicios, puertos y documentación Swagger

| Microservicio | Puerto | Ruta en el Gateway | Swagger UI (local) |
|---|---|---|---|
| Eureka Server | 8761 | — | `http://localhost:8761` (dashboard) |
| API Gateway | 8080 | — | — |
| Usuario | 8081 | `/v1/usuarios/**` | `http://localhost:8081/usuario/swagger-ui.html` |
| Categoria | 8082 | `/api/categorias/**` | `http://localhost:8082/categoria/swagger-ui.html` |
| Producto | 8083 | `/api/productos/**` | `http://localhost:8083/producto/swagger-ui.html` |
| Inventario | 8084 | `/v1/inventario/**` | `http://localhost:8084/inventario/swagger-ui.html` |
| Carrito | 8085 | `/api/carrito/**` | `http://localhost:8085/carrito/swagger-ui.html` |
| Pedido | 8086 | `/v1/pedidos/**` | `http://localhost:8086/pedido/swagger-ui.html` |
| Pagos | 8087 | `/v1/pagos/**` | `http://localhost:8087/pagos/swagger-ui.html` |
| Envio | 8088 | `/v1/envios/**` | `http://localhost:8088/envio/swagger-ui.html` |
| Notificacion | 8089 | `/api/notificaciones/**` | `http://localhost:8089/notificacion/swagger-ui.html` |
| Reporte | 8090 | `/api/reportes/**` | `http://localhost:8090/reporte/swagger-ui.html` |

> Todas las rutas también son accesibles a través del Gateway en `http://localhost:8080<ruta>`, por ejemplo `http://localhost:8080/v1/usuarios`.

## Comunicación entre microservicios

| Servicio | Depende de |
|---|---|
| Producto | Categoria (valida que la categoría exista al crear/actualizar) |
| Pedido | Usuario (valida que el usuario exista) |
| Envio | Usuario, Pedido (valida ambos antes de generar el envío) |
| Inventario | Producto (valida que el producto exista antes de crear inventario) |
| Carrito | Usuario |
| Notificacion | Usuario |
| Pagos | Pedido |

---

## Ejecución local (sin Docker)

### Requisitos previos

- JDK 21
- Maven (se usa el wrapper `mvnw`/`mvnw.cmd` incluido en cada módulo)
- MySQL corriendo en `localhost:3306` (en este proyecto se usa **Laragon**)
- IDE recomendado: IntelliJ IDEA o VS Code

### 1. Crear las bases de datos

Cada microservicio usa su propia base de datos y las crea/actualiza automáticamente con **Flyway** al arrancar (no es necesario crear las tablas a mano, solo la base de datos vacía):

```sql
CREATE DATABASE db_usuario;
CREATE DATABASE db_categoria;
CREATE DATABASE db_producto;
CREATE DATABASE db_inventario;
CREATE DATABASE db_carrito;
CREATE DATABASE db_pedido;
CREATE DATABASE db_pagos;
CREATE DATABASE db_envio;
CREATE DATABASE db_notificacion;
CREATE DATABASE db_reporte;
```

Usuario/contraseña por defecto: `root` / sin contraseña (configurable en cada `application.yml`).

### 2. Levantar los servicios (orden recomendado)

1. **Eureka Server** primero (puerto 8761) — los demás servicios necesitan registrarse en él al arrancar.
2. **API Gateway**.
3. Los **10 microservicios de dominio**, en cualquier orden (cada uno se registra en Eureka al iniciar).

En cada módulo:

```powershell
.\mvnw.cmd clean package -DskipTests
.\mvnw.cmd spring-boot:run
```

O ejecutando directamente la clase `*Application.java` desde el IDE.

### 3. Verificar que todo está arriba

- Dashboard de Eureka: `http://localhost:8761` — deben aparecer los 11 servicios registrados (10 microservicios + Gateway).
- Probar una ruta a través del Gateway: `http://localhost:8080/v1/usuarios`.

---

## Ejecución con Docker (Docker Compose)

### Requisitos previos

- Docker Desktop instalado y corriendo
- MySQL accesible desde los contenedores. En este proyecto MySQL **no se dockeriza**: se usa el MySQL de Laragon corriendo en el host, y los contenedores acceden a él vía `host.docker.internal`.

### Pasos

1. Crear las mismas 10 bases de datos indicadas arriba en el MySQL del host (Laragon).
2. Desde la raíz del repositorio, levantar todo el ecosistema:

```bash
docker compose up --build
```

Esto construye y levanta los 12 contenedores (Eureka, Gateway y los 10 microservicios) en una red `bridge` llamada `backend`. El `docker-compose.yml` define:

- `healthcheck` sobre Eureka, para evitar que el resto de los servicios intenten registrarse antes de que esté listo.
- `extra_hosts: host.docker.internal:host-gateway` en cada microservicio, para que puedan llegar al MySQL del host desde dentro del contenedor.
- Variables de entorno por servicio (`EUREKA_URL`, `SPRING_DATASOURCE_URL`, y las URLs internas de los microservicios con los que se comunica cada uno vía Docker DNS, por ejemplo `http://microservice-usuario:8081`).

3. Verificar que los contenedores están arriba:

```bash
docker compose ps
```

4. Acceder igual que en local, pero usando los mismos puertos publicados (`8080` para el Gateway, `8761` para Eureka, etc.), ya que cada contenedor expone su puerto al host.

5. Para detener todo:

```bash
docker compose down
```

---

## Testing

Los microservicios usan **JUnit 5 + Mockito** para pruebas unitarias de la capa de servicio (lógica de negocio), siguiendo la estructura **Given–When–Then**, con mocks de repositorios y de los `Client`/`WebClient` usados para comunicación entre servicios.

Para correr los tests de un microservicio:

```powershell
.\mvnw.cmd test
```

> Cobertura actual: Usuario y Producto cuentan con suites de test completas sobre su capa de servicio. Categoria, Reporte, Pedido, Envio e Inventario están en proceso de cobertura. Carrito, Pagos y Notificacion quedan pendientes.

---

## Estructura del repositorio

```
EvaluacionParcial3FullStack/
├── Eureka-Server/
├── Api-Gateway/
├── MicroService-Usuario/
├── MicroService-Categoria/
├── MicroService-Producto/
├── MicroService-Inventario/
├── MicroService-Carrito/
├── MicroService-Pedido/
├── MicroService-Pagos/
├── MicroService-Envio/
├── MicroService-Notificacion/
├── MicroService-Reporte/
├── docker-compose.yml
└── README.md
```

Cada microservicio sigue la misma estructura interna:

```
src/main/java/.../
├── Controller/   # expone los endpoints REST
├── Service/      # lógica de negocio
├── Repository/   # acceso a datos (Spring Data JPA)
├── Model/        # entidades JPA
├── Dto/          # objetos de transferencia (Request/Response)
├── Mapper/       # conversión Entidad ↔ DTO
├── Client/       # clientes WebClient para comunicación con otros microservicios
├── Exception/    # excepciones de negocio y manejador global
└── Config/       # configuración de Swagger/OpenAPI
```
