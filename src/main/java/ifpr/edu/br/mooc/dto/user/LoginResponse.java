package ifpr.edu.br.mooc.dto.user;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        UserResponse user
) {
    public LoginResponse(String accessToken, Long expiresIn, UserResponse user) {
        this(accessToken, "Bearer", expiresIn, user);
    }
}