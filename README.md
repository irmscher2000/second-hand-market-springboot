# SecondHandMarket

Marketplace de compraventa de artículos de segunda mano, desarrollado con **Spring Boot** y **Thymeleaf**. Los usuarios pueden registrarse, publicar sus propios productos a la venta, explorar el catálogo, añadir artículos al carrito y completar compras.

## 🚀 Demo en vivo

🔗 **[Ver demo](https://second-hand-market-springboot.onrender.com)**

**Usuario de prueba:**
- Email: `demo@example.com`
- Contraseña: `demo1234`

> ⚠️ Al ser un servicio gratuito, el primer acceso puede tardar unos segundos en "despertar" si lleva tiempo sin uso.

## 📋 Índice

- [Qué puedes probar](#qué-puedes-probar)
- [Tecnologías](#tecnologías)
- [Ejecutar en local](#ejecutar-en-local)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Rutas principales](#rutas-principales)
- [Capturas de pantalla](#capturas-de-pantalla)

## Qué puedes probar

Siguiendo este orden podrás ver el flujo completo de la aplicación:

1. **Explorar el catálogo sin registrarte** — entra en la página principal y navega por los productos publicados.
2. **Registrarte** — crea una cuenta nueva con tu nombre, email y una foto de perfil (opcional).
3. **Publicar un producto** — sube un artículo con nombre, precio y foto.
4. **Comprar un producto** — añade artículos al carrito de otro usuario de prueba y finaliza la compra para generar una factura.
5. **Consultar tus compras** — revisa el historial y las facturas generadas.

## Tecnologías

- **Backend:** Java 17+, Spring Boot, Spring Security, Spring Data JPA
- **Vistas:** Thymeleaf
- **Base de datos:** PostgreSQL (producción) / H2 (desarrollo local)
- **Gestión de archivos:** subida de imágenes de productos y avatares
- **Despliegue:** Docker + Render.com

## Ejecutar en local

### Requisitos
- JDK 17 o superior (recomendado JDK 21+)
- Maven (incluido el wrapper `mvnw` / `mvnw.cmd`, no necesitas instalarlo aparte)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/irmscher2000/second-hand-market-springboot.git
cd second-hand-market-springboot

# 2. Ejecutar la aplicación (usa H2 en archivo por defecto)
./mvnw spring-boot:run        # Linux/macOS
.\mvnw.cmd spring-boot:run     # Windows
```

La aplicación arrancará en `http://localhost:9000`.

### Ejecutar los tests

```bash
./mvnw test        # Linux/macOS
.\mvnw.cmd test      # Windows
```

### Generar el JAR ejecutable

```bash
./mvnw package
java -jar target/secondhandmarket-0.0.1-SNAPSHOT.jar
```

## Estructura del proyecto

```
src/main/java/market/secondhandmarket/
├── controladores/
│   ├── LoginController        # Inicio de sesión
│   ├── ProductoController      # Listado, alta y edición de productos
│   ├── CompraController        # Carrito y proceso de compra
│   ├── ZonaPublicaController   # Vistas públicas (home, catálogo)
│   └── FilesController         # Subida y descarga de archivos
src/main/resources/
├── templates/                  # Vistas Thymeleaf (HTML)
├── static/                     # CSS, JS e imágenes estáticas
└── application.properties      # Configuración general
```

## Rutas principales

### Públicas (sin necesidad de registro)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Redirige a la página principal |
| GET | `/public/` | Catálogo de productos disponibles |
| GET | `/public/producto/{id}` | Detalle de un producto |
| GET | `/files/{filename}` | Descarga de una imagen subida |

### Autenticación

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/auth/login` | Formulario de inicio de sesión |
| POST | `/auth/register` | Registro de usuario (nombre, email, contraseña, avatar) |

### Gestión de productos (requiere sesión iniciada)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/app/misproductos` | Productos publicados por el usuario (admite `?q=` para buscar) |
| GET | `/app/producto/nuevo` | Formulario para publicar un producto |
| POST | `/app/producto/nuevo/submit` | Crear producto (nombre, precio, imagen) |
| GET | `/app/producto/editar/{id}` | Editar un producto propio |
| GET | `/app/misproductos/{id}/eliminar` | Eliminar un producto propio |

### Carrito y compras (requiere sesión iniciada)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/app/carrito` | Ver carrito actual |
| GET | `/app/carrito/add/{id}` | Añadir producto al carrito |
| GET | `/app/carrito/eliminar/{id}` | Quitar producto del carrito |
| GET | `/app/carrito/finalizar` | Finalizar compra y generar factura |
| GET | `/app/miscompras` | Historial de compras |
| GET | `/app/compra/factura/{id}` | Ver factura de una compra |


---

Proyecto desarrollado como práctica personal para consolidar conocimientos de Spring Boot, Spring Security y arquitectura MVC en Java.