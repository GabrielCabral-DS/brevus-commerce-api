package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.ProductRequestDTO;
import br.com.brevus.commerce_api.dto.ProductResponseDTO;
import br.com.brevus.commerce_api.dto.UsersResponseDTO;
import br.com.brevus.commerce_api.mapper.ProductMapper;
import br.com.brevus.commerce_api.model.Category;
import br.com.brevus.commerce_api.model.Product;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.CategoryRepository;
import br.com.brevus.commerce_api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    public Product register(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        product.setCategory(category);

        return productRepository.save(product);
    }

    public List<ProductResponseDTO> listAllProduct(){
        List<Product> productList = productRepository.findAll();
        return productMapper.toResponseDtoList(productList);
    }


    public List<ProductResponseDTO> lisProductsByDate() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Product> productList = productRepository.findAll(pageRequest).getContent();

        return productMapper.toResponseDtoList(productList);
    }


    public Product update(UUID id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        product.setName(dto.name());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setImagePath(dto.imagePath());
        product.setDescription(dto.description());
        product.setCategory(category);


        return productRepository.save(product);
    }

    public void delete(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    public Page<ProductResponseDTO> getProducts(int page, int size, String search, String categoryId) {
        int adjustedPage = Math.max(0, page);

        Pageable pageable = PageRequest.of(
                adjustedPage,
                size,
                Sort.by(Sort.Direction.ASC, "name")
        );

        String searchFilter = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        UUID categoryUuid = null;
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            try {
                categoryUuid = UUID.fromString(categoryId);
            } catch (IllegalArgumentException e) {
                categoryUuid = null;
            }
        }

        Page<Product> productPage = productRepository.findByCategoryAndName(categoryUuid, searchFilter, pageable);

        return productPage.map(productMapper::toResponseDto);
    }


}
