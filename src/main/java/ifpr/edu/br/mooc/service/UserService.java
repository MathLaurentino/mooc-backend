package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.user.CreateUserRequest;
import ifpr.edu.br.mooc.dto.user.UpdateUserRequest;
import ifpr.edu.br.mooc.dto.user.UpdateUserStatusRequest;
import ifpr.edu.br.mooc.dto.user.UserResponse;
import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.entity.enums.UserRole;
import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.exceptions.user.DuplicateCpfException;
import ifpr.edu.br.mooc.exceptions.user.DuplicateEmailException;
import ifpr.edu.br.mooc.mapper.UserMapper;
import ifpr.edu.br.mooc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.email());

        validateNewUser(request);

        String cleanCpf = request.cpf().replaceAll("[^0-9]", "");

        User user = userMapper.toEntity(request);
        user.setCpf(cleanCpf);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUserRole(UserRole.STUDENT);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateCurrentUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user data for user id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        validateUpdateUser(user, request);

        String cleanCpf = request.cpf().replaceAll("[^0-9]", "");

        userMapper.updateEntityFromDto(request, user);
        user.setCpf(cleanCpf);

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", updatedUser.getId());

        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public UserResponse updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (user.getUserRole() != UserRole.STUDENT) {
            throw new BadRequestException("Apenas usuários com perfil de ALUNO podem ter o status alterado");
        }

        user.setActive(request.active());

        User updatedUser = userRepository.save(user);
        log.info("User status updated successfully. User id: {}, new status: {}", updatedUser.getId(), updatedUser.getActive());
        return userMapper.toResponse(updatedUser);
    }

    @Transactional(readOnly = true)
    public long countStudents() {
        return userRepository.countByUserRole(UserRole.STUDENT);
    }

    private void validateNewUser(CreateUserRequest request) {
        // Clean CPF for validation
        String cleanCpf = request.cpf().replaceAll("[^0-9]", "");
        
        // Check if email already exists (RF02)
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }
        
        // Check if CPF already exists (RF02)
        if (userRepository.existsByCpf(cleanCpf)) {
            throw new DuplicateCpfException();
        }
    }

    private void validateUpdateUser(User user, UpdateUserRequest request) {
        String cleanCpf = request.cpf().replaceAll("[^0-9]", "");

        if (!user.getEmail().equals(request.email())) {
            userRepository.findByEmail(request.email())
                    .ifPresent(existingUser -> { throw new DuplicateEmailException();});
        }

        if (!user.getCpf().equals(cleanCpf)) {
            userRepository.findByCpf(cleanCpf)
                    .ifPresent(existingUser -> {throw new DuplicateCpfException();});
        }
    }
}