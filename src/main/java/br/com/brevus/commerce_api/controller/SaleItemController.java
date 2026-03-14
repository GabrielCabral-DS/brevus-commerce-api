package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.SaleItemResponseDTO;
import br.com.brevus.commerce_api.service.SaleItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }


    @GetMapping
    public ResponseEntity<List<SaleItemResponseDTO>> listAllSaleItems(){
        List<SaleItemResponseDTO> saleItemResponseDTOList = saleItemService.listALLSaleItems();
        return ResponseEntity.ok().body(saleItemResponseDTOList);
    }
}
