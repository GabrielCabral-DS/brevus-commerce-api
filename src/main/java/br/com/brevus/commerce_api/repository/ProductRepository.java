package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
