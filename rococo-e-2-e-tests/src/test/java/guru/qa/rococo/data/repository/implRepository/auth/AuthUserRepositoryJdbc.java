package guru.qa.rococo.data.repository.implRepository.auth;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.auth.AuthAuthorityDao;
import guru.qa.rococo.data.dao.auth.AuthUserDao;
import guru.qa.rococo.data.dao.implDao.auth.AuthAuthorityDaoJdbc;
import guru.qa.rococo.data.dao.implDao.auth.AuthUserDaoJdbc;
import guru.qa.rococo.data.entity.auth.AuthAuthorityEntity;
import guru.qa.rococo.data.entity.auth.AuthUserEntity;
import guru.qa.rococo.data.repository.AuthUserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        authUserDao.create(user);
        authAuthorityDao.create(user.getAuthorities().toArray(new AuthAuthorityEntity[0]));
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        return authUserDao.update(user);
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        Optional<AuthUserEntity> user = authUserDao.findById(id);
        user.ifPresent(u ->
                u.addAuthorities(authAuthorityDao.findAllByUserId(id)
                        .toArray(new AuthAuthorityEntity[0])));
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        Optional<AuthUserEntity> user = authUserDao.findByUsername(username);
        user.ifPresent(u ->
                u.addAuthorities(authAuthorityDao.findAllByUserId(u.getId())
                        .toArray(new AuthAuthorityEntity[0])));
        return user;
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.remove(user);
    }
}
