package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.SaleItemResponseDTO;
import br.com.brevus.commerce_api.service.SaleItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-items")
@Tag(name = "Sale Items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "List", description = "List all sale items")
    public ResponseEntity<List<SaleItemResponseDTO>> listAllSaleItems(){
        List<SaleItemResponseDTO> saleItemResponseDTOList = saleItemService.listALLSaleItems();
        return ResponseEntity.ok().body(saleItemResponseDTOList);
    }
}
