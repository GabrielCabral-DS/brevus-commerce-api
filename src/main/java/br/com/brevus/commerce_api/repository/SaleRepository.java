package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
}
