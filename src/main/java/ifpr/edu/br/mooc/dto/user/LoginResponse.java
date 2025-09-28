package ifpr.edu.br.mooc.dto.user;

public record LoginResponse(
        String access_token,
        String token_type,
        Long expires_in,
        UserResponse user
) {
    public LoginResponse(String accessToken, Long expiresIn, UserResponse user) {
        this(accessToken, "Bearer", expiresIn, user);
    }
}