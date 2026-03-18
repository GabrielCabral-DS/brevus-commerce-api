package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.ProductRequestDTO;
import br.com.brevus.commerce_api.dto.ProductResponseDTO;
import br.com.brevus.commerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/products")
@RestController
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Register", description = "Register a new product")
    public ResponseEntity<Void> registerProduct(@Valid @RequestBody ProductRequestDTO dto){
        productService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List", description = "List all products")
    public ResponseEntity<List<ProductResponseDTO>> listAllProducts(){
        List<ProductResponseDTO> productResponseDTOList = productService.listAllProduct();
        return ResponseEntity.ok().body(productResponseDTOList);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'CLIENT')")
    @Operation(summary = "List with Pagination", description = "List all products with pagination")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId) {

        return ResponseEntity.ok(productService.getProducts(page, size, search, categoryId));
    }

    @GetMapping("/recents")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'CLIENT')")
    @Operation(summary = "List Recent Products", description = "List recent products")
    public ResponseEntity<List<ProductResponseDTO>> listProductsByDate(){
        List<ProductResponseDTO> productResponseDTOList = productService.lisProductsByDate();
        return ResponseEntity.ok().body(productResponseDTOList);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Update", description = "Update product")
    public ResponseEntity<Void> updateProduct(@PathVariable(value = "id") UUID id, @Valid @RequestBody ProductRequestDTO dto){
        productService.update(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Delete", description = "Delete product")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") UUID id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
