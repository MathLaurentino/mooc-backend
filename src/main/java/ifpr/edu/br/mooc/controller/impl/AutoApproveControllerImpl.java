package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.AutoApproveController;
import ifpr.edu.br.mooc.dto.autoApprove.AutoApproveResponseDto;
import ifpr.edu.br.mooc.dto.autoApprove.AutoApproveUpdateDto;
import ifpr.edu.br.mooc.service.AutoApproveConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auto-approve")
@RequiredArgsConstructor
@Slf4j
public class AutoApproveControllerImpl implements AutoApproveController {

    private final AutoApproveConfigService autoApproveConfigService;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutoApproveResponseDto> getAutoApproveStatus() {
        log.info("Request to get auto-approve status");
        AutoApproveResponseDto response = autoApproveConfigService.getAutoApproveStatus();
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AutoApproveResponseDto> updateAutoApproveStatus(
            @RequestBody @Valid AutoApproveUpdateDto dto
    ) {
        log.info("Request to update auto-approve status to: {}", dto.enabled());
        AutoApproveResponseDto response = autoApproveConfigService.updateAutoApproveStatus(dto);
        return ResponseEntity.ok(response);
    }
}