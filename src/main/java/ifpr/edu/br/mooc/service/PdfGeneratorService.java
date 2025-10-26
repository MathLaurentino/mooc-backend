package ifpr.edu.br.mooc.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import ifpr.edu.br.mooc.entity.Certificate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfGeneratorService {

    private final QRCodeService qrCodeService;

    @Value("${server.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DeviceRgb GREEN_COLOR = new DeviceRgb(76, 175, 80); // Verde IFPR

    /**
     * Gera o PDF do certificado
     */
    public byte[] generateCertificatePdf(Certificate certificate) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Criar documento PDF em paisagem (landscape)
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Configurar página em paisagem (A4 rotacionado)
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

            Document document = new Document(pdfDoc);
            document.setMargins(50, 50, 50, 50);

            // Adicionar metadados do certificado
            addMetadata(pdfDoc, certificate);

            // Fontes
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Título "CERTIFICADO"
            Paragraph title = new Paragraph("CERTIFICADO")
                    .setFont(boldFont)
                    .setFontSize(36)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(60)
                    .setMarginBottom(20);
            document.add(title);

            // Subtítulo
            Paragraph subtitle = new Paragraph("MOOC IFPR - Plataforma de Cursos Massivos Online do IFPR Campus Foz do Iguaçu")
                    .setFont(regularFont)
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(subtitle);

            // "Certifica que:"
            Paragraph certifies = new Paragraph("Certifica que:")
                    .setFont(regularFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(certifies);

            // Texto principal com dados do aluno
            String mainText = String.format(
                    "%s, CPF %s concluiu o curso %s, " +
                            "ofertado pelo campus de %s, com carga horária de %s horas na data de %s.",
                    certificate.getStudentName(),
                    formatCpf(certificate.getStudentCpf()),
                    certificate.getCourseName(),
                    certificate.getCampusName(),
                    certificate.getWorkload(),
                    certificate.getCompletionDate().format(DATE_FORMATTER)
            );

            Paragraph mainContent = new Paragraph(mainText)
                    .setFont(regularFont)
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginLeft(80)
                    .setMarginRight(80)
                    .setMarginBottom(40);
            document.add(mainContent);

            // Local e data de emissão
            String issueDate = String.format("FOZ DO IGUAÇU, PR, %s",
                    java.time.LocalDate.now().format(DATE_FORMATTER));

            Paragraph location = new Paragraph(issueDate)
                    .setFont(regularFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(location);

            // QR Code
            String validationUrl = String.format("%s/mooc/certificates/validate/%s",
                    baseUrl, certificate.getId());
            byte[] qrCodeBytes = qrCodeService.generateQRCode(validationUrl);
            Image qrCode = new Image(ImageDataFactory.create(qrCodeBytes));
            qrCode.setWidth(120);
            qrCode.setHeight(120);
            qrCode.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);
            qrCode.setMarginRight(50);
            document.add(qrCode);

            // Código do certificado (abaixo do QR Code)
            Paragraph certificateCode = new Paragraph(String.format("Código: %s", certificate.getId()))
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginRight(50)
                    .setMarginTop(5);
            document.add(certificateCode);

            document.close();

            log.info("PDF generated successfully for certificate ID: {}", certificate.getId());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF for certificate ID: {}", certificate.getId(), e);
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    /**
     * Adiciona metadados criptográficos ao PDF
     */
    private void addMetadata(PdfDocument pdfDoc, Certificate certificate) {
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("Certificado - " + certificate.getCourseName());
        info.setAuthor("MOOC IFPR");
        info.setSubject("Certificado de Conclusão");
        info.setCreator("MOOC IFPR Platform");

        // Adicionar dados de verificação como metadados customizados
        Map<String, String> customMetadata = new HashMap<>();
        customMetadata.put("CertificateId", certificate.getId().toString());
        customMetadata.put("StudentName", certificate.getStudentName());
        customMetadata.put("StudentCpf", certificate.getStudentCpf());
        customMetadata.put("CourseName", certificate.getCourseName());
        customMetadata.put("Workload", certificate.getWorkload());
        customMetadata.put("CampusName", certificate.getCampusName());
        customMetadata.put("CompletionDate", certificate.getCompletionDate().format(DATE_FORMATTER));
        customMetadata.put("Algorithm", "SHA256withRSA");
        customMetadata.put("Hash", certificate.getDocumentHash());
        customMetadata.put("Signature", certificate.getDigitalSignature());
        customMetadata.put("PublicKey", certificate.getPublicKey());

        // Adicionar metadados customizados
        for (Map.Entry<String, String> entry : customMetadata.entrySet()) {
            info.setMoreInfo(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Formata CPF para exibição (XXX.XXX.XXX-XX)
     */
    private String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11)
        );
    }
}