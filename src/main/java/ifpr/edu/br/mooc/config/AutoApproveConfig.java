package ifpr.edu.br.mooc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoApproveConfig {
    private Boolean enabled;
    private LocalDateTime enabledAt;
    private LocalDateTime disabledAt;
}