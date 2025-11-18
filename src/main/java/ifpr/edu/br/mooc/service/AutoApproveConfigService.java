package ifpr.edu.br.mooc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ifpr.edu.br.mooc.config.AutoApproveConfig;
import ifpr.edu.br.mooc.dto.autoApprove.AutoApproveResponseDto;
import ifpr.edu.br.mooc.dto.autoApprove.AutoApproveUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AutoApproveConfigService {

    private final ObjectMapper objectMapper;
    private final String configFilePath;
    private final CertificateRequestService certificateRequestService;

    public AutoApproveConfigService(
            @Value("${auto-approve.config-path:src/main/resources/config/auto-approve-config.json}") String configPath,
            @Lazy CertificateRequestService certificateRequestService
    ) {
        this.configFilePath = configPath;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.certificateRequestService = certificateRequestService;
        initializeConfigFile();
    }

    /**
     * Inicializa o arquivo de configuração se ele não existir
     */
    private void initializeConfigFile() {
        File configFile = new File(configFilePath);

        // Cria o diretório se não existir
        File parentDir = configFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Cria o arquivo com configuração padrão se não existir
        if (!configFile.exists()) {
            try {
                AutoApproveConfig defaultConfig = new AutoApproveConfig(false, null, null);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, defaultConfig);
                log.info("Arquivo de configuração de aprovação automática criado: {}", configFilePath);
            } catch (IOException e) {
                log.error("Erro ao criar arquivo de configuração de aprovação automática", e);
            }
        }
    }

    /**
     * Verifica se o modo de aprovação automática está ativo
     */
    public boolean isAutoApproveEnabled() {
        try {
            AutoApproveConfig config = readConfig();
            return config.getEnabled() != null && config.getEnabled();
        } catch (IOException e) {
            log.error("Erro ao ler configuração de aprovação automática", e);
            return false;
        }
    }

    /**
     * Atualiza o status do modo de aprovação automática
     */
    public AutoApproveResponseDto updateAutoApproveStatus(AutoApproveUpdateDto dto) {
        try {
            AutoApproveConfig config = readConfig();

            boolean wasEnabled = config.getEnabled() != null && config.getEnabled();
            boolean willBeEnabled = dto.enabled();

            config.setEnabled(willBeEnabled);

            if (willBeEnabled && !wasEnabled) {
                // Está ativando agora
                config.setEnabledAt(LocalDateTime.now());
                log.info("Modo de aprovação automática ATIVADO");

                // Aprova automaticamente todas as solicitações pendentes
                certificateRequestService.autoApproveAllPendingRequests();

            } else if (!willBeEnabled && wasEnabled) {
                // Está desativando agora
                config.setDisabledAt(LocalDateTime.now());
                log.info("Modo de aprovação automática DESATIVADO");
            }

            writeConfig(config);

            return new AutoApproveResponseDto(
                    config.getEnabled(),
                    config.getEnabledAt(),
                    config.getDisabledAt()
            );

        } catch (IOException e) {
            log.error("Erro ao atualizar configuração de aprovação automática", e);
            throw new RuntimeException("Erro ao atualizar configuração de aprovação automática", e);
        }
    }

    /**
     * Obtém o status atual do modo de aprovação automática
     */
    public AutoApproveResponseDto getAutoApproveStatus() {
        try {
            AutoApproveConfig config = readConfig();
            return new AutoApproveResponseDto(
                    config.getEnabled(),
                    config.getEnabledAt(),
                    config.getDisabledAt()
            );
        } catch (IOException e) {
            log.error("Erro ao ler configuração de aprovação automática", e);
            throw new RuntimeException("Erro ao ler configuração de aprovação automática", e);
        }
    }

    /**
     * Lê o arquivo de configuração
     */
    private AutoApproveConfig readConfig() throws IOException {
        File configFile = new File(configFilePath);
        return objectMapper.readValue(configFile, AutoApproveConfig.class);
    }

    /**
     * Escreve no arquivo de configuração
     */
    private void writeConfig(AutoApproveConfig config) throws IOException {
        File configFile = new File(configFilePath);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, config);
    }
}