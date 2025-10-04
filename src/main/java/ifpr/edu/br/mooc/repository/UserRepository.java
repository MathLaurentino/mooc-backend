package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    Optional<User> findByEmailAndActiveTrue(String email);

    boolean existsByEmailAndUserRole(String email, UserRole userRole);
}