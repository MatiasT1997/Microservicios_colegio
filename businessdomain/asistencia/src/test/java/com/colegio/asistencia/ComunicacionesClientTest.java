package com.colegio.asistencia;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.colegio.asistencia.service.ComunicacionesClient;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifica que el metodo de respaldo del Circuit Breaker devuelve una respuesta
 * degradada (lista vacia) en lugar de propagar el error.
 */
class ComunicacionesClientTest {

    @Test
    @DisplayName("El fallback devuelve lista vacia cuando Comunicaciones falla")
    void fallbackDevuelveListaVacia() throws Exception {
        ComunicacionesClient client = new ComunicacionesClient(null);

        // El fallback es privado; lo invocamos por reflexion para probar su contrato
        Method fallback = ComunicacionesClient.class
                .getDeclaredMethod("fallbackMensajes", Long.class, Throwable.class);
        fallback.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> resultado =
                (List<Map<String, Object>>) fallback.invoke(client, 10L, new RuntimeException("servicio caido"));

        assertTrue(resultado.isEmpty());
    }
}
