package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.ProductRequestDTO;
import br.com.brevus.commerce_api.dto.ProductResponseDTO;
import br.com.brevus.commerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/products")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<Void> registerProduct(@Valid @RequestBody ProductRequestDTO dto){
        productService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listAllProducts(){
        List<ProductResponseDTO> productResponseDTOList = productService.listAllProduct();
        return ResponseEntity.ok().body(productResponseDTOList);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId) {

        return ResponseEntity.ok(productService.getProducts(page, size, search, categoryId));
    }

    @GetMapping("/recents")
    public ResponseEntity<List<ProductResponseDTO>> listProductsByDate(){
        List<ProductResponseDTO> productResponseDTOList = productService.lisProductsByDate();
        return ResponseEntity.ok().body(productResponseDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(value = "id") UUID id, @Valid @RequestBody ProductRequestDTO dto){
        productService.update(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") UUID id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
