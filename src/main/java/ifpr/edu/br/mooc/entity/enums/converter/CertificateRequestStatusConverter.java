package ifpr.edu.br.mooc.entity.enums.converter;

import ifpr.edu.br.mooc.entity.enums.CertificateRequestStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CertificateRequestStatusConverter implements AttributeConverter<CertificateRequestStatus, String> {

    @Override
    public String convertToDatabaseColumn(CertificateRequestStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public CertificateRequestStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return CertificateRequestStatus.fromCode(dbData);
    }
}