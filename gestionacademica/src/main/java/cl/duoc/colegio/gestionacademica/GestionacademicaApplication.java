package cl.duoc.colegio.gestionacademica;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Gestion Academica API",
		version = "1.0",
		description = "Microservicio de cursos, asignaturas, evaluaciones y notas - Libro de Clases Digital"
))
public class GestionacademicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionacademicaApplication.class, args);
	}

}
