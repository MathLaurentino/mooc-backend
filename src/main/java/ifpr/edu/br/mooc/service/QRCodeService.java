package ifpr.edu.br.mooc.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class QRCodeService {

    private static final int QR_CODE_SIZE = 200;

    /**
     * Gera um QR Code como array de bytes
     * @param content Conteúdo do QR Code (URL de validação)
     * @return Array de bytes da imagem do QR Code
     */
    public byte[] generateQRCode(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE,
                    hints
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            log.info("QR Code generated successfully");
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating QR Code", e);
            throw new RuntimeException("Failed to generate QR Code", e);
        }
    }
}