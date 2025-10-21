package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;

@Slf4j
@Service
public class LocalFileStorageService {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Value("${file.storage.base-path:C:/mooc-uploads}")
    private String basePath;

    /**
     * Salva a thumbnail do curso no sistema de arquivos
     * @param file arquivo de imagem
     * @param courseId ID do curso
     * @return caminho relativo do arquivo salvo
     */
    public String saveCourseThumbnail(MultipartFile file, Long courseId) {
        validateFile(file);
        
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String relativePath = String.format("course/%d/thumbnail.%s", courseId, fileExtension);
        
        Path fullPath = Paths.get(basePath, relativePath);
        
        try {
            // Cria os diretórios se não existirem
            Files.createDirectories(fullPath.getParent());
            
            // Salva o arquivo (sobrescreve se já existir)
            Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Thumbnail salva com sucesso: {}", fullPath);
            return relativePath;
            
        } catch (IOException e) {
            log.error("Erro ao salvar thumbnail do curso {}: {}", courseId, e.getMessage());
            throw new BadRequestException("Erro ao salvar imagem do curso.");
        }
    }

    /**
     * Deleta a thumbnail do curso
     * @param relativePath caminho relativo do arquivo
     */
    public void deleteCourseThumbnail(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        
        try {
            Path fullPath = Paths.get(basePath, relativePath);
            Files.deleteIfExists(fullPath);
            log.info("Thumbnail deletada: {}", fullPath);
        } catch (IOException e) {
            log.error("Erro ao deletar thumbnail: {}", e.getMessage());
        }
    }

    /**
     * Valida o arquivo de imagem
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Arquivo de imagem é obrigatório.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("Arquivo muito grande. Tamanho máximo: 5MB.");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException(
                String.format("Extensão não permitida. Permitidas: %s", String.join(", ", ALLOWED_EXTENSIONS))
            );
        }

        // Valida o content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("O arquivo deve ser uma imagem.");
        }
    }

    /**
     * Carrega a thumbnail do curso do sistema de arquivos
     * @param relativePath caminho relativo do arquivo
     * @return Resource com o arquivo
     */
    public Resource loadCourseThumbnail(String relativePath) {
        try {
            Path fullPath = Paths.get(basePath, relativePath);
            Resource resource = new UrlResource(fullPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new EntityNotFoundException("Imagem não encontrada.");
            }
        } catch (MalformedURLException e) {
            log.error("Erro ao carregar thumbnail: {}", e.getMessage());
            throw new EntityNotFoundException("Imagem não encontrada.");
        }
    }

    /**
     * Extrai a extensão do arquivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BadRequestException("Nome de arquivo inválido.");
        }
        
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}