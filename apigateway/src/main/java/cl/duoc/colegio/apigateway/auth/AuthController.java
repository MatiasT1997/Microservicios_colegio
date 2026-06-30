package cl.duoc.colegio.apigateway.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtUtil.generarToken(username);
            return Mono.just(ResponseEntity.ok(Map.of("token", token)));
        }
        return Mono.just(ResponseEntity.status(401).body(Map.of("error", "Credenciales invalidas")));
    }
}
