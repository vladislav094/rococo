package guru.qa.rococo.data.repository.implRepository.userdata;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.implDao.userdata.UserdataUserDaoJdbc;
import guru.qa.rococo.data.dao.userdata.UserdataUserDao;
import guru.qa.rococo.data.entity.userdata.UserEntity;
import guru.qa.rococo.data.repository.UserdataUserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    @Override
    @NotNull
    public UserEntity create(@NotNull UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    @NotNull
    public UserEntity update(@NotNull UserEntity user) {
        return userdataUserDao.update(user);
    }

    @Override
    public Optional<UserEntity> findById(@NotNull UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(@NotNull String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public void remove(@NotNull UserEntity user) {
        userdataUserDao.remove(user);
    }
}
