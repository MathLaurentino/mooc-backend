package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.pageable.PageResponse;
import ifpr.edu.br.mooc.dto.user.*;
import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<UserResponse> createUser(CreateUserRequest request);

    ResponseEntity<UserResponse> updateCurrentUser(UpdateUserRequest request);

    ResponseEntity<UserResponse> updateUserStatus(Long userId, UpdateUserStatusRequest request);

    ResponseEntity<Long> countStudents();

    ResponseEntity<UserResponse> getCurrentUserData();

    ResponseEntity<PageResponse<UserListResponse>> getAllStudents(
            String name,
            String email,
            Boolean active,
            Integer page,
            Integer size,
            String direction
    );
}
