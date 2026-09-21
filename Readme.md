# Mis Raíces - Tienda Online

"Mis Raíces" es una aplicación web de comercio electrónico desarrollada como proyecto integrador en Java utilizando el ecosistema de Spring Boot. La plataforma ofrece un catálogo dinámico con filtros avanzados, gestión de usuarios, carrito de compras y un diseño estético de estilo vintage.

---

## Tecnologías y Herramientas Utilizadas

* **Backend:** Java 17, Spring Boot 3, Spring Security, Spring Data JPA.
* **Base de Datos:** MySQL.
* **Frontend / Vistas:** Thymeleaf, HTML5, CSS3, Bootstrap 5, Bootstrap Icons.
* **Control de Versiones:** Git & GitHub.
* **Entorno de Desarrollo:** IntelliJ IDEA.

---

## Estructura del Proyecto

La arquitectura del proyecto está organizada en paquetes limpios siguiendo buenas prácticas de desarrollo en capas:

```text
Mis-raices-main/
│
├── src/
│   ├── main/
│   │   ├── java/com/tienda/
│   │   │   ├── config/              # Clases de configuración (Seguridad, Global Controller, Web)
│   │   │   ├── controller/          # Controladores web (Autenticación, Carrito, Productos, etc.)
│   │   │   ├── model/               # Entidades y modelos de datos JPA (Usuario, Producto, Pedido, etc.)
│   │   │   ├── repository/          # Interfaces de persistencia de datos (Spring Data JPA)
│   │   │   └── service/             # Lógica de negocio y especificaciones de filtrado
│   │   │
│   │   └── resources/
│   │       ├── static/              # Archivos estáticos (Hojas de estilo CSS e imágenes)
│   │       │   └── estilos/         # (carrito.css, productos.css, style-index.css, login-registro.css, etc.)
│   │       ├── templates/           # Vistas HTML dinámicas con Thymeleaf
│   │       │   └── (carrito.html, index.html, productos.html, fragmentos.html, login.html, etc.)
│   │       └── application.properties # Configuración de conexión a base de datos y servidor
│   │
│   └── test/                        # Pruebas unitarias e de integración
│
├── pom.xml                          # Dependencias y gestión de Maven
└── README.md                        # Documentación oficial del proyecto
```

## Características Principales
- **Seguridad y Autenticación:** Registro y login de usuarios con cifrado seguro de contraseñas mediante Spring Security.
- **Catálogo y Filtrado Interactivo:** Visualización de productos con filtros por categoría, rango de precios (con actualización dinámica en la interfaz mediante JavaScript) y selección múltiple de colores.
- **Gestión de Carrito:** Agregar productos, modificar cantidades y persistir las compras de forma asociada al usuario autenticado.
- **Diseño Responsivo:** Interfaz adaptada a dispositivos móviles y de escritorio con componentes de Bootstrap 5 y estilos personalizados.

## Guía de Clonación y Ejecución Local
Para poner en marcha este proyecto en tu entorno local, sigue los pasos a continuación:

1. Clonar el repositorio

Abre tu terminal o Git Bash y ejecuta el siguiente comando:

```bash
git clone [https://github.com/ChiaraBaldasarre/Mis-raices-main.git](https://github.com/ChiaraBaldasarre/Mis-raices-main.git)
```

2. Configurar la Base de Datos

- Asegúrate de tener instalado MySQL y un gestor (como MySQL Workbench o DBeaver).
- Crea una base de datos vacía (por ejemplo: mis_raices_db).
- Abre el archivo de configuración ubicado en src/main/resources/application.properties y ajusta tus credenciales locales de conexión:

```Properties
spring.datasource.url=jdbc:mysql://localhost:3306/mis_raices_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

3. Compilar y Ejecutar el Proyecto

- Desde tu IDE (IntelliJ IDEA / Eclipse / VS Code): Abre la carpeta del proyecto, busca la clase principal MisRaicesApplication.java dentro de src/main/java/com/tienda/ y haz clic en Run.
- Desde la Terminal utilizando Maven:

```Bash
mvn spring-boot:run
```

4. Acceder a la Aplicación

Una vez que el servidor Spring Boot haya iniciado correctamente, abre tu navegador web e ingresa a:
```text
http://localhost:8080
```

```markdown
## Autora
* **Chiara Baldasarre** - *Desarrollo Fullstack*
```
