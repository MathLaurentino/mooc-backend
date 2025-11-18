package ifpr.edu.br.mooc.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CertificateRequestStatus {
    ANALYSIS("analise", "Em Análise"),
    APPROVED("aprovado", "Aprovado"),
    REJECTED("reprovado", "Reprovado");

    private final String code;
    private final String description;

    public static CertificateRequestStatus fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return ANALYSIS;
        }

        for (CertificateRequestStatus status : CertificateRequestStatus.values()) {
            if (status.code.equalsIgnoreCase(code) || status.description.equalsIgnoreCase(code)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid certificate request status code: " + code);
    }
}