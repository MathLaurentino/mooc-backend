package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.AuthController;
import ifpr.edu.br.mooc.dto.user.LoginRequest;
import ifpr.edu.br.mooc.dto.user.LoginResponse;
import ifpr.edu.br.mooc.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for email: {}", request.email());
        LoginResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}