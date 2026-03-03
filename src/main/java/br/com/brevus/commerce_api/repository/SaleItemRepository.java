package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {
}
