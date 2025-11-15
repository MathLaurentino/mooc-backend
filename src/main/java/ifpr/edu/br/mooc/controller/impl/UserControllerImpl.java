package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.UserController;
import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.dto.user.*;
import ifpr.edu.br.mooc.repository.specification.UserSpecification;
import ifpr.edu.br.mooc.security.CurrentUserService;
import ifpr.edu.br.mooc.security.JwtUtils;
import ifpr.edu.br.mooc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Received request to create user with email: {}", request.email());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        Long userId = currentUserService.getCurrentUserId();
        log.info("Received request to update user data for user id: {}", userId);
        UserResponse response = userService.updateCurrentUser(userId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        log.info("Received request to update user status for user id: {}", userId);
        UserResponse response = userService.updateUserStatus(userId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/count/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countStudents() {
        Long count = userService.countStudents();
        return ResponseEntity.ok(count);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserListResponse>> getAllStudents(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        log.info("Received request to list students");

        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var spec = new UserSpecification(name, email, active);

        PageResponse<UserListResponse> response = userService.getAllStudents(spec, pageable);

        return ResponseEntity.ok(response);
    }
}