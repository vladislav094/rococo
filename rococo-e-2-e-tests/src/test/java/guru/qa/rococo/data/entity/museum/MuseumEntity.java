package guru.qa.rococo.data.entity.museum;

import guru.qa.rococo.model.rest.MuseumJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.MERGE;

@Getter
@Setter
@Entity
@Table(name = "museum")
public class MuseumEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "title", unique = true, nullable = false, length = 50)
    private String title;

    @Column(name = "description", unique = true, nullable = false, length = 255)
    private String description;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = MERGE)
    @JoinColumn(name = "geo_id", referencedColumnName = "id", nullable = false)
    private GeoEntity geo;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MuseumEntity that = (MuseumEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Nonnull
    public static MuseumEntity fromJson(@Nonnull MuseumJson museum) {
        MuseumEntity museumEntity = new MuseumEntity();
        museumEntity.setId(museum.id());
        museumEntity.setTitle(museum.title());
        museumEntity.setDescription(museum.description());
        museumEntity.setPhoto(museum.photo() != null ? museum.photo().getBytes(StandardCharsets.UTF_8) : null);
        museumEntity.setGeo(GeoEntity.fromJson(museum.geo()));
        return museumEntity;
    }
}