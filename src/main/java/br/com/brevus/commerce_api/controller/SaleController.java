package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.SaleRequestDTO;
import br.com.brevus.commerce_api.dto.SaleResponseDTO;
import br.com.brevus.commerce_api.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;


    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }


    @PostMapping
    public ResponseEntity<Void> regiterSale(@Valid @RequestBody SaleRequestDTO dto){
        saleService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> listAllSales(){
        List<SaleResponseDTO> saleResponseDTOList = saleService.listAllSales();
        return ResponseEntity.ok().body(saleResponseDTOList);
    }
}
