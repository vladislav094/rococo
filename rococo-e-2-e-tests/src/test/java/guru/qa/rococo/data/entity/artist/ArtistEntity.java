package guru.qa.rococo.data.entity.artist;

import guru.qa.rococo.model.rest.ArtistJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "biography", nullable = false, length = 2000)
    private String biography;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    public static ArtistEntity fromJson(ArtistJson artistJson) {
        ArtistEntity ae = new ArtistEntity();
        ae.setId(artistJson.id());
        ae.setName(artistJson.name());
        ae.setBiography(artistJson.biography());
        ae.setPhoto(artistJson.photo().getBytes(StandardCharsets.UTF_8));
        return ae;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ArtistEntity that = (ArtistEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
