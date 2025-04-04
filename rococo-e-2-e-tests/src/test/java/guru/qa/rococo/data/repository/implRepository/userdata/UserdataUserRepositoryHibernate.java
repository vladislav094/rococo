package guru.qa.rococo.data.repository.implRepository.userdata;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.userdata.UserEntity;
import guru.qa.rococo.data.jpa.EntityManagers;
import guru.qa.rococo.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.userdataJdbcUrl());

    @Override
    public UserEntity create(UserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public UserEntity update(UserEntity user) {
        entityManager.joinTransaction();
        return entityManager.merge(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("select u from UserEntity u where u.username =: username", UserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(UserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
    }
}
