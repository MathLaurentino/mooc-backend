package ifpr.edu.br.mooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class CryptographyService {

    private static final String ALGORITHM = "RSA";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;

    private KeyPair keyPair;

    public CryptographyService() {
        try {
            // Gera par de chaves ao inicializar o serviço
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(KEY_SIZE);
            this.keyPair = keyGen.generateKeyPair();
            log.info("RSA key pair generated successfully");
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating RSA key pair", e);
            throw new RuntimeException("Failed to initialize cryptography service", e);
        }
    }

    /**
     * Gera o hash SHA-256 dos dados do certificado
     */
    public String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating hash", e);
            throw new RuntimeException("Failed to generate hash", e);
        }
    }

    /**
     * Assina o hash com a chave privada
     */
    public String signHash(String hash) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(keyPair.getPrivate());
            signature.update(Base64.getDecoder().decode(hash));
            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);
        } catch (Exception e) {
            log.error("Error signing hash", e);
            throw new RuntimeException("Failed to sign hash", e);
        }
    }

    /**
     * Retorna a chave pública em formato Base64
     */
    public String getPublicKeyAsString() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * Verifica a assinatura digital
     */
    public boolean verifySignature(String hash, String signature, String publicKeyString) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(Base64.getDecoder().decode(hash));

            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }

    /**
     * Concatena os dados do certificado para gerar o hash
     */
    public String buildCertificateData(
            String studentName,
            String studentCpf,
            String courseName,
            String workload,
            String campusName,
            String completionDate
    ) {
        return String.join("|",
                studentName,
                studentCpf,
                courseName,
                workload,
                campusName,
                completionDate
        );
    }
}