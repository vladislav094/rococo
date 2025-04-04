package guru.qa.rococo.data.dao.userdata;

import guru.qa.rococo.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDao {

    UserEntity create(UserEntity user);

    UserEntity update(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAll();

    void remove(UserEntity user);
}
