package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.SaleRequestDTO;
import br.com.brevus.commerce_api.dto.SaleResponseDTO;
import br.com.brevus.commerce_api.model.Sale;
import br.com.brevus.commerce_api.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;


    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }


    @PostMapping
    public ResponseEntity<Map<String, UUID>> registerSale(@Valid @RequestBody SaleRequestDTO dto){
        Sale sale = saleService.register(dto);
        Map<String, UUID> response = new HashMap<>();
        response.put("id", sale.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> listAllSales(){
        List<SaleResponseDTO> saleResponseDTOList = saleService.listAllSales();
        return ResponseEntity.ok().body(saleResponseDTOList);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> listSalesByClientId(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){

        Page<SaleResponseDTO> salesPage = saleService.listAllSalesByClientId(clientId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("orders", salesPage.getContent());
        response.put("totalPages", salesPage.getTotalPages());
        response.put("currentPage", salesPage.getNumber());
        response.put("totalItems", salesPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
