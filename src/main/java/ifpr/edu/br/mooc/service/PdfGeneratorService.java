package ifpr.edu.br.mooc.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import ifpr.edu.br.mooc.entity.Certificate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
    private static final DeviceRgb TEXT_COLOR = new DeviceRgb(64, 64, 64); // Cinza escuro


    /**
     * Gera o PDF do certificado usando o template base
     */
    public byte[] generateCertificatePdf(Certificate certificate) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Carregar o template PDF do classpath
            ClassPathResource templateResource = new ClassPathResource("templates/certificado-template.pdf");
            InputStream templateStream = templateResource.getInputStream();

            PdfReader reader = new PdfReader(templateStream);
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(reader, writer);

            // Adicionar metadados do certificado
            addMetadata(pdfDoc, certificate);

            Document document = new Document(pdfDoc);

            // Fontes - usando as fontes padrão mais próximas das solicitadas
            // Crimson Pro não está disponível, usamos Times (serifada similar)
            PdfFont crimsonFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont crimsonBoldFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

            // Calibri não está disponível, usamos Helvetica (sem serifa, similar)
            PdfFont calibriFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Arial = Helvetica
            PdfFont arialFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // ===== SUBTÍTULO =====
            // "MOOC IFPR - Plataforma de Cursos Massivos Online do IFPR Campus Foz do Iguaçu"
            Paragraph subtitle = new Paragraph("MOOC IFPR - PLATAFORMA DE CURSOS MASSIVOS ONLINE DO IFPR CAMPUS FOZ DO IGUAÇU")
                    .setFont(crimsonFont)
                    .setFontSize(11)
                    .setFontColor(TEXT_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(120, 390, 600);
            document.add(subtitle);

            // ===== CERTIFICA QUE =====
            Paragraph certifies = new Paragraph("CERTIFICA QUE:")
                    .setFont(crimsonFont)
                    .setFontSize(12)
                    .setFontColor(TEXT_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(120, 360, 600); // Movido para baixo: 450 -> 415
            document.add(certifies);

            // ===== TEXTO PRINCIPAL =====
            // Construir o texto com os dados do certificado
            String mainText = String.format(
                    "%s, CPF %s CONCLUIU O CURSO %s, " +
                            "OFERTADO PELO CAMPOS DE %s, COM " +
                            "CARGA HORÁRIA DE %s HORAS NA DATA DE %s.",
                    certificate.getStudentName().toUpperCase(),
                    formatCpf(certificate.getStudentCpf()),
                    certificate.getCourseName().toUpperCase(),
                    certificate.getCampusName().toUpperCase().replace("IFPR - CAMPUS ", "").replace("IFPR - CÂMPUS ", ""),
                    certificate.getWorkload(),
                    certificate.getCompletionDate().format(DATE_FORMATTER)
            );

            Paragraph mainContent = new Paragraph(mainText)
                    .setFont(calibriFont)
                    .setFontSize(16)
                    .setFontColor(TEXT_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(140, 260, 560); // Movido para baixo: 320 -> 285
            document.add(mainContent);

            // ===== QR CODE =====
            String validationUrl = String.format("%s/mooc/certificates/validate/code/%s",
                    baseUrl, certificate.getId());
            byte[] qrCodeBytes = qrCodeService.generateQRCode(validationUrl);
            Image qrCode = new Image(ImageDataFactory.create(qrCodeBytes))
                    .setWidth(100)
                    .setHeight(100)
                    .setFixedPosition(175, 110); // Movido para baixo: 140 -> 110
            document.add(qrCode);

            // ===== NÚMERO DO CERTIFICADO =====
            // Alinhado na parte inferior do QR Code (QR Code termina em y=110, então texto em y=120)
            Paragraph certificateNumber = new Paragraph("N° do certificado: " + certificate.getId())
                    .setFont(arialFont)
                    .setFontSize(9)
                    .setFontColor(TEXT_COLOR)
                    .setFixedPosition(295, 130, 500); // Alinhado com a parte inferior do QR: y=130
            document.add(certificateNumber);

            // ===== URL DE VERIFICAÇÃO =====
            String shortUrl = validationUrl.replace(baseUrl + "/mooc/", "");
            Paragraph verificationUrl = new Paragraph("URL de verificação: " + shortUrl)
                    .setFont(arialFont)
                    .setFontSize(9)
                    .setFontColor(TEXT_COLOR)
                    .setFixedPosition(295, 113, 500); // Logo abaixo do número: y=113
            document.add(verificationUrl);

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
        // A chave pública NÃO é mais incluída aqui
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