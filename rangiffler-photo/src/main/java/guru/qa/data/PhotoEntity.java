package guru.qa.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "country_id")
    @PrimaryKeyJoinColumn(name = "country_id")
    private UUID countryId;

    private byte[] photo;

    @Column(name = "description")
    private String description;

    private String username;

}
