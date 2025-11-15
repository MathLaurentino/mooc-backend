package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.user.CreateUserRequest;
import ifpr.edu.br.mooc.dto.user.UpdateUserRequest;
import ifpr.edu.br.mooc.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserController {

    ResponseEntity<UserResponse> createUser(CreateUserRequest request);

    ResponseEntity<UserResponse> updateCurrentUser(UpdateUserRequest request);

    ResponseEntity<Long> countStudents();
}
