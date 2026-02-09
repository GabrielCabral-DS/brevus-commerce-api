package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
