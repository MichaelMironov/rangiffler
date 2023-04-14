package rangiffler.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Data
@ToString(exclude = "authorities")
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean AccountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean CredentialsNonExpired;

    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<AuthorityEntity> authorities = new ArrayList<>();

    public void addAuthorities(AuthorityEntity... authorities) {
        this.authorities.addAll(List.of(authorities));
    }
}
