package guru.qa.rococo.data.repository.implRepository.userdata;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.implDao.userdata.UserdataUserDaoJdbc;
import guru.qa.rococo.data.dao.userdata.UserdataUserDao;
import guru.qa.rococo.data.entity.userdata.UserEntity;
import guru.qa.rococo.data.repository.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userdataUserDao.update(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.remove(user);
    }
}
