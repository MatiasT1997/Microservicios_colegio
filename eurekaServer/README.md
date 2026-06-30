# eurekaServer

Servidor de Service Discovery del sistema Libro de Clases Digital, basado en Netflix Eureka.

## Responsabilidades

- Registro dinamico de los microservicios (apigateway, gestionacademica, asistencia, comunicaciones)
- Permite que los servicios se descubran entre si por nombre, sin direcciones IP fijas

## Requisitos previos

- Java 17
- Maven 3.9+

## Instalacion y ejecucion

Debe ser el **primer servicio en levantarse**, ya que todos los demas dependen de su disponibilidad para registrarse.

Desde la carpeta `eurekaServer`:

```
mvn clean install
mvn spring-boot:run
```

O bien, en NetBeans: abrir el proyecto y ejecutar Run sobre `eurekaServer`.

## Verificacion

Una vez levantado, abrir en el navegador:

```
http://localhost:8761
```

Debe mostrar el panel de Eureka. A medida que se levantan los demas microservicios, deberian aparecer listados como UP en la seccion "Instances currently registered with Eureka".

## Nota sobre NetBeans

Al cerrar y volver a abrir NetBeans, la propiedad Main Class del proyecto puede resetearse a `${start-class}`. Si esto ocurre, ir a Project Properties > Run > Main Class > Browse, y volver a seleccionar la clase principal (`EurekaServerApplication`).
