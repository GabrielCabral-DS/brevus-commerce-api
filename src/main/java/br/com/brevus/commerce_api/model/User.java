package br.com.brevus.commerce_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    private String name;
    private String email;
    private String password;
    private String phone;

    @Column(name = "date_birth")
    private LocalDate dateBirth;

    @Column(name = "date_registered")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateRegistered;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

    @PrePersist
    public void prePersist() {
        dateRegistered = LocalDate.now();
    }
}
