package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
    @Query("""
SELECT DISTINCT s
FROM Sale s
LEFT JOIN FETCH s.items i
LEFT JOIN FETCH i.product
LEFT JOIN FETCH s.client
LEFT JOIN FETCH s.seller
""")
    List<Sale> findAllComplete();

    boolean existsByDeliveryAddressId(UUID id);


    @Query("""
SELECT s
FROM Sale s
LEFT JOIN FETCH s.items
WHERE s.client.id = :clientId
""")
    Page<Sale> findAllByClientId(UUID clientId, Pageable pageable);

}
