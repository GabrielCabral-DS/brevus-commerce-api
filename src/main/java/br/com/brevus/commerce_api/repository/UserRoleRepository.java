package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    @Query("SELECT ur.role.name FROM UserRole ur WHERE ur.user.id = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") UUID userId);
}
