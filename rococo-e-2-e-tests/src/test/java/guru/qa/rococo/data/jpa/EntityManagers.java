package guru.qa.rococo.data.jpa;

import guru.qa.rococo.data.jdbc.DataSources;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManagers {

    private EntityManagers() {
    }

    private static final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

    @SuppressWarnings("resource")
    @Nonnull
    public static EntityManager em(@Nonnull String jdbcUrl) {
        return new ThreadSafeEntityManager(emfs.computeIfAbsent(
                jdbcUrl,
                key -> {
                    // Сначала регистрируем DataSource в JNDI
                    DataSource ds = DataSources.dataSource(jdbcUrl);

                    // Затем создаём EntityManagerFactory
                    final String persistenceUnitName = StringUtils.substringAfter(jdbcUrl, "5432/");
                    Map<String, Object> props = new HashMap<>();
                    props.put("jakarta.persistence.nonJtaDataSource", ds);
                    return Persistence.createEntityManagerFactory(persistenceUnitName, props);
                }
        ).createEntityManager()
        );
    }

    public static void closeAllEmfs() {
        emfs.values().forEach(EntityManagerFactory::close);
    }
}
