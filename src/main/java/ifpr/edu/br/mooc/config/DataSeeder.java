package ifpr.edu.br.mooc.config;

import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.entity.enums.UserRole;
import ifpr.edu.br.mooc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
    }

    private void seedAdminUser() {
        // Verificar se já existe um admin
        if (userRepository.existsByEmailAndUserRole("admin@mooc.ifpr.edu.br", UserRole.ADMIN)) {
            log.info("Admin user already exists, skipping seed");
            return;
        }

        // Criar usuário admin
        User adminUser = User.builder()
                .fullName("Administrador do Sistema")
                .cpf("00000000000")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("admin@mooc.ifpr.edu.br")
                .password(passwordEncoder.encode("admin123")) // Senha será criptografada
                .active(true)
                .userRole(UserRole.ADMIN)
                .build();

        userRepository.save(adminUser);
        
        log.info("Admin user created successfully:");
        log.info("Email: admin@mooc.ifpr.edu.br");
        log.info("Password: admin123");
    }
}