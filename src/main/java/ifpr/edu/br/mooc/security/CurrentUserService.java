package ifpr.edu.br.mooc.security;

import ifpr.edu.br.mooc.entity.User;
import ifpr.edu.br.mooc.exceptions.base.UnauthorizedException;
import ifpr.edu.br.mooc.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrentUserService {

    private final UserRepository userRepository;

    /**
     * Get the currently authenticated user principal (from JWT, no DB call)
     * @return JwtUserPrincipal with basic user info from token
     * @throws UnauthorizedException if no user is authenticated
     */
    public JwtUserPrincipal getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No authenticated user found in security context");
            throw new UnauthorizedException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtUserPrincipal) {
            return (JwtUserPrincipal) principal;
        }

        log.error("Principal is not of type JwtUserPrincipal: {}", principal.getClass().getName());
        throw new UnauthorizedException("Invalid authentication principal");
    }

    /**
     * Get the full User entity from database (use sparingly)
     * This method DOES make a database call, use only when full user data is needed
     * @return User entity from database
     */
    @Transactional(readOnly = true)
    public User getCurrentUserEntity() {
        JwtUserPrincipal principal = getCurrentUserPrincipal();
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }

    /**
     * Get the ID of the currently authenticated user (no DB call)
     * @return ID of the authenticated user
     */
    public Long getCurrentUserId() {
        return getCurrentUserPrincipal().getId();
    }

    /**
     * Get the email of the currently authenticated user (no DB call)
     * @return Email of the authenticated user
     */
    public String getCurrentUserEmail() {
        return getCurrentUserPrincipal().getEmail();
    }

    /**
     * Check if a user is the current authenticated user (no DB call)
     * @param userId User ID to check
     * @return true if the user is the current authenticated user
     */
    public boolean isCurrentUser(Long userId) {
        try {
            return getCurrentUserId().equals(userId);
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    /**
     * Load fresh user data from database (use when you need updated data)
     * @return Fresh User entity from database
     */
    @Transactional(readOnly = true)
    public User refreshCurrentUser() {
        return getCurrentUserEntity();
    }
}