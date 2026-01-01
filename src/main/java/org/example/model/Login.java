package org.example.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "login")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false,name="password_hash")
    private String password;
    @Column(nullable = false,name="id_status")
    private Integer idStatus;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of();
    }
}
