package br.com.brevus.commerce_api.validation;

import br.com.brevus.commerce_api.exceptions.DuplicateRecordException;
import br.com.brevus.commerce_api.model.Product;
import br.com.brevus.commerce_api.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductValidation {

    private final ProductRepository productRepository;

    public ProductValidation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Product product){
        if (existsByName(product)){
            throw new DuplicateRecordException("Já existe um produto cadastrado com esse nome");
        }
    }

    public boolean existsByName(Product product){
        Optional<Product> productOptional = productRepository.findByName(product.getName());

        if (product.getId() == null){
            productOptional.isPresent();
        }

        return productOptional.isPresent() && !product.getId().equals(productOptional.get().getId());
    }
}
