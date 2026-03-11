package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
