package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, UUID> {
    List<LoginHistory> findByUserId(UUID id);
}
