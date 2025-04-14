package guru.qa.rococo.data.repository.implRepository.auth;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.auth.AuthUserEntity;
import guru.qa.rococo.data.jpa.EntityManagers;
import guru.qa.rococo.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    @PersistenceContext
    private final EntityManager entityManager = EntityManagers.em(CFG.authJdbcUrl());

    @NotNull
    @Override
    @Transactional
    public AuthUserEntity create(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @NotNull
    @Override
    @Transactional
    public AuthUserEntity update(AuthUserEntity user) {
        entityManager.joinTransaction();
        return entityManager.merge(user);
    }

    @Override
    @Transactional
    public Optional<AuthUserEntity> findById(@NotNull UUID id) {
        return Optional.ofNullable(entityManager.find(AuthUserEntity.class, id));
    }

    @Override
    @Transactional
    public Optional<AuthUserEntity> findByUsername(@NotNull String username) {
        try {
            return Optional.of(
                    entityManager.createQuery(
                                    "select u from AuthUserEntity u where u.username =: username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void remove(@NotNull AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(user);
    }
}
