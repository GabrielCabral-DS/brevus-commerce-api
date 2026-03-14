package br.com.brevus.commerce_api.repository;

import br.com.brevus.commerce_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Page<User> findByUserRoles_Role_Name(String client, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.userRoles ur JOIN ur.role r " +
            "WHERE r.name = :roleName " +
            "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findByRoleAndSearch(@Param("roleName") String roleName,
                                   @Param("search") String search,
                                   Pageable pageable);

}
