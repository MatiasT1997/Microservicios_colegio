package cl.duoc.colegio.asistencia;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Asistencia API",
		version = "1.0",
		description = "Microservicio de asistencia y anotaciones de conducta - Libro de Clases Digital"
))
public class AsistenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsistenciaApplication.class, args);
	}

}
