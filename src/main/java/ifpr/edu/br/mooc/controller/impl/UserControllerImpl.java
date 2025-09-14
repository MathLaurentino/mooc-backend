package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.UserController;
import ifpr.edu.br.mooc.dto.user.CreateUserRequest;
import ifpr.edu.br.mooc.dto.user.UserResponse;
import ifpr.edu.br.mooc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Received request to create user with email: {}", request.email());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}