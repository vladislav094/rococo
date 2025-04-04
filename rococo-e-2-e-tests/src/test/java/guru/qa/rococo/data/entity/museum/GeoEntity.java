package guru.qa.rococo.data.entity.museum;

import guru.qa.rococo.model.rest.GeoJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "geo")
public class GeoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "city", unique = true, nullable = false, length = 50)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private CountryEntity country;

    @OneToMany(mappedBy = "geo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MuseumEntity> museums;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        GeoEntity geoEntity = (GeoEntity) o;
        return getId() != null && Objects.equals(getId(), geoEntity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Nonnull
    public static GeoEntity fromJson(@Nonnull GeoJson geoJson) {
        GeoEntity ge = new GeoEntity();
        ge.setId(geoJson.id());
        ge.setCity(geoJson.city());
        ge.setCountry(CountryEntity.fromJson(geoJson.country()));

        return ge;
    }
}
