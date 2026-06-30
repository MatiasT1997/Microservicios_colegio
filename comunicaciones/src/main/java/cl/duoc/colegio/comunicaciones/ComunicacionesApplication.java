package cl.duoc.colegio.comunicaciones;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Comunicaciones API",
		version = "1.0",
		description = "Microservicio de mensajes y notificaciones - Libro de Clases Digital"
))
public class ComunicacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComunicacionesApplication.class, args);
	}

}
