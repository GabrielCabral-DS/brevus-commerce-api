package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
