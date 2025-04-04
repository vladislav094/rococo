package guru.qa.rococo.data.repository.implRepository.auth;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.auth.AuthAuthorityDao;
import guru.qa.rococo.data.dao.auth.AuthUserDao;
import guru.qa.rococo.data.dao.implDao.auth.AuthAuthorityDaoSpringJdbc;
import guru.qa.rococo.data.dao.implDao.auth.AuthUserDaoSpringJdbc;
import guru.qa.rococo.data.entity.auth.AuthAuthorityEntity;
import guru.qa.rococo.data.entity.auth.AuthUserEntity;
import guru.qa.rococo.data.extractor.AuthUserEntityExtractor;
import guru.qa.rococo.data.repository.AuthUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.data.jdbc.DataSources.dataSource;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "SELECT a.id as authority_id," +
                                " authority," +
                                " user_id as id," +
                                " u.username," +
                                " u.password," +
                                " u.enabled," +
                                " u.account_non_expired," +
                                " u.account_non_locked," +
                                " u.credentials_non_expired " +
                                "FROM \"user\" u join public.authority a on u.id = a.user_id WHERE u.id = ?",
                        AuthUserEntityExtractor.instance,
                        id
                )
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "SELECT a.id as authority_id," +
                                " authority," +
                                " user_id as id," +
                                " u.username," +
                                " u.password," +
                                " u.enabled," +
                                " u.account_non_expired," +
                                " u.account_non_locked," +
                                " u.credentials_non_expired " +
                                "FROM \"user\" u join public.authority a on u.id = a.user_id WHERE u.username = ?",
                        AuthUserEntityExtractor.instance,
                        username
                )
        );
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.remove(user);
    }
}
