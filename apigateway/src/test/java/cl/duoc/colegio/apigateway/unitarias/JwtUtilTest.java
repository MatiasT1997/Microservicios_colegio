package cl.duoc.colegio.apigateway.unitarias;

import cl.duoc.colegio.apigateway.auth.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas Unitarias - JwtUtil
 * Verifican la generacion, validacion y extraccion de
 * datos de tokens JWT sin necesitar el contexto de Spring.
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void debeGenerarTokenNoNulo() {
        String token = jwtUtil.generarToken("admin");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void debeGenerarTokenConFormatoJWT() {
        String token = jwtUtil.generarToken("admin");

        String[] partes = token.split("\\.");
        assertEquals(3, partes.length, "Un JWT debe tener 3 partes separadas por puntos");
    }

    @Test
    void debeValidarTokenRecienGenerado() {
        String token = jwtUtil.generarToken("admin");

        assertTrue(jwtUtil.validarToken(token));
    }

    @Test
    void debeRechazarTokenInvalido() {
        assertFalse(jwtUtil.validarToken("token.invalido.falso"));
    }

    @Test
    void debeRechazarTokenVacio() {
        assertFalse(jwtUtil.validarToken(""));
    }

    @Test
    void debeExtraerUsernameCorrectamente() {
        String token = jwtUtil.generarToken("admin");

        String username = jwtUtil.obtenerUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void debeMantenerUsernameDistintoParaCadaUsuario() {
        String tokenAdmin = jwtUtil.generarToken("admin");
        String tokenProfe = jwtUtil.generarToken("profe");

        assertEquals("admin", jwtUtil.obtenerUsername(tokenAdmin));
        assertEquals("profe", jwtUtil.obtenerUsername(tokenProfe));
    }
}
