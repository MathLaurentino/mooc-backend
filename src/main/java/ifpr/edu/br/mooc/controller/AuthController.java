package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.user.LoginRequest;
import ifpr.edu.br.mooc.dto.user.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    public ResponseEntity<LoginResponse> login(LoginRequest request);

}
