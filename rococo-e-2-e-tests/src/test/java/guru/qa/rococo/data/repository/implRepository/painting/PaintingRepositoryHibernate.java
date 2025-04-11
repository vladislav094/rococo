package guru.qa.rococo.data.repository.implRepository.painting;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.data.jpa.EntityManagers;
import guru.qa.rococo.data.repository.PaintingRepository;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PaintingRepositoryHibernate implements PaintingRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PaintingRepositoryHibernate.class);

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.paintingJdbcUrl());

    @NotNull
    @Override
    @Transactional
    public PaintingEntity create(@NotNull PaintingEntity painting) {
        entityManager.joinTransaction();
        entityManager.persist(painting);
        return painting;
    }

    @Override
    @Transactional
    public Optional<PaintingEntity> findById(@NotNull UUID id) {
        return Optional.ofNullable(entityManager.find(PaintingEntity.class, id));
    }

    @Override
    @Transactional
    public Optional<PaintingEntity> findByTitle(@NotNull String title) {
        Objects.requireNonNull(title, "Painting title cannot be null");
        try {
            return entityManager.createQuery("SELECT p FROM PaintingEntity p WHERE p.title = :title", PaintingEntity.class)
                    .setParameter("title", title.trim())
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            LOG.error("Error finding PaintingEntity by title: {}", title, e);
            return Optional.empty();
        }
    }
}
