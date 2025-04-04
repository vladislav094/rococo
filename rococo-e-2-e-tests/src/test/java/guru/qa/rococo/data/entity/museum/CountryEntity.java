package guru.qa.rococo.data.entity.museum;

import guru.qa.rococo.model.rest.CountryJson;
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
@Table(name = "country")
public class CountryEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "UUID default uuid_generate_v1()")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 60)
    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GeoEntity> geo;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CountryEntity that = (CountryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Nonnull
    public static CountryEntity fromJson(@Nonnull CountryJson countryJson) {
        CountryEntity ce = new CountryEntity();
        ce.setId(countryJson.id());
        ce.setName(countryJson.name());

        return ce;
    }
}
