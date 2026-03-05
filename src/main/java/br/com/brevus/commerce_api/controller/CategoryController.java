package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.CategoryReponseDTO;
import br.com.brevus.commerce_api.dto.CategoryRequestDTO;
import br.com.brevus.commerce_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> registerCategory(@Valid @RequestBody CategoryRequestDTO dto){
        categoryService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryReponseDTO>> listAllcategories(){
        List<CategoryReponseDTO> categoryRequestDTOList = categoryService.listAllCategories();
        return ResponseEntity.ok().body(categoryRequestDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategories(@PathVariable(value = "id")UUID id, @Valid @RequestBody CategoryRequestDTO dto){
        categoryService.update(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategories(@PathVariable(value = "id") UUID id){
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
