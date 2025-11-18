package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.autoApprove.AutoApproveResponseDto;
import ifpr.edu.br.mooc.dto.autoApprove.AutoApproveUpdateDto;
import org.springframework.http.ResponseEntity;

public interface AutoApproveController {

    ResponseEntity<AutoApproveResponseDto> getAutoApproveStatus();

    ResponseEntity<AutoApproveResponseDto> updateAutoApproveStatus(AutoApproveUpdateDto dto);
}