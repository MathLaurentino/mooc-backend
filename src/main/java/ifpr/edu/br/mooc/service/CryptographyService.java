package ifpr.edu.br.mooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Value("${crypto.keys.storage-path:./keys}")
    private String keysStoragePath;

    private static final String PRIVATE_KEY_FILE = "private_key.pem";
    private static final String PUBLIC_KEY_FILE = "public_key.pem";

    private KeyPair keyPair;

    public CryptographyService() {
        // Construtor vazio - inicialização via @PostConstruct
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        try {
            Path keysDir = Paths.get(keysStoragePath);
            Path privateKeyPath = keysDir.resolve(PRIVATE_KEY_FILE);
            Path publicKeyPath = keysDir.resolve(PUBLIC_KEY_FILE);

            // Verifica se as chaves já existem
            if (Files.exists(privateKeyPath) && Files.exists(publicKeyPath)) {
                log.info("Loading existing RSA keys from {}", keysStoragePath);
                this.keyPair = loadKeyPair(privateKeyPath, publicKeyPath);
                log.info("RSA keys loaded successfully");
            } else {
                log.info("Generating new RSA key pair and saving to {}", keysStoragePath);
                this.keyPair = generateAndSaveKeyPair(keysDir, privateKeyPath, publicKeyPath);
                log.info("New RSA key pair generated and saved successfully");
            }
        } catch (Exception e) {
            log.error("Error initializing cryptography service", e);
            throw new RuntimeException("Failed to initialize cryptography service", e);
        }
    }

    private KeyPair generateAndSaveKeyPair(Path keysDir, Path privateKeyPath, Path publicKeyPath)
            throws NoSuchAlgorithmException, IOException {
        // Cria o diretório se não existir
        Files.createDirectories(keysDir);

        // Gera o par de chaves
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Salva a chave privada
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyPair.getPrivate().getEncoded()) +
                "\n-----END PRIVATE KEY-----";
        Files.writeString(privateKeyPath, privateKeyPEM);

        // Salva a chave pública
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyPair.getPublic().getEncoded()) +
                "\n-----END PUBLIC KEY-----";
        Files.writeString(publicKeyPath, publicKeyPEM);

        log.info("Keys saved to disk");
        return keyPair;
    }

    private KeyPair loadKeyPair(Path privateKeyPath, Path publicKeyPath) throws Exception {
        // Lê a chave privada
        String privateKeyPEM = Files.readString(privateKeyPath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // Lê a chave pública
        String publicKeyPEM = Files.readString(publicKeyPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
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