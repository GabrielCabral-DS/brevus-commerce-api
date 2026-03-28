package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.LoginHistoryResponseDTO;
import br.com.brevus.commerce_api.service.LoginHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/audited")
@Tag(name = "Audited")
public class AuditedController {

    private final LoginHistoryService loginHistoryService;

    public AuditedController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @GetMapping("/list-all/login")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Login", description = "Audited all logins by users")
    public ResponseEntity<List<LoginHistoryResponseDTO>> listAllHistory(){
        List<LoginHistoryResponseDTO> dtoList = loginHistoryService.listAllLogins();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/lis-all/login/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Login", description = "Audited all logins by users by email")
    public ResponseEntity<List<LoginHistoryResponseDTO>> listAllHistoryByUserId(@PathVariable(value = "email") String email){
        List<LoginHistoryResponseDTO> dtoList = loginHistoryService.listAllByUserId(email);
        return ResponseEntity.ok().body(dtoList);
    }


}
