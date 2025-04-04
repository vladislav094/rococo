package guru.qa.rococo.data.dao.implDao.auth;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.auth.AuthAuthorityDao;
import guru.qa.rococo.data.entity.auth.AuthAuthorityEntity;
import guru.qa.rococo.data.mapper.AuthAuthorityEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static guru.qa.rococo.data.jdbc.DataSources.dataSource;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.authJdbcUrl();

    @Override
    public void create(AuthAuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(url));
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUser().getId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public List<AuthAuthorityEntity> findById(UUID id) {
        return new JdbcTemplate(dataSource(url)).query(
                "SELECT * FROM authority WHERE id = ?",
                AuthAuthorityEntityRowMapper.instance,
                id
        );
    }

    @Override
    public List<AuthAuthorityEntity> findAllByUserId(UUID userId) {
        return new JdbcTemplate(dataSource(url)).query(
                "SELECT * FROM authority WHERE user_id = ?",
                AuthAuthorityEntityRowMapper.instance,
                userId
        );
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        return new JdbcTemplate(dataSource(url)).query(
                "SELECT * FROM authority",
                AuthAuthorityEntityRowMapper.instance
        );
    }


    @Override
    public void remove(AuthAuthorityEntity user) {
        new JdbcTemplate(dataSource(url)).update(
                "DELETE FROM authority WHERE id = ?",
                user.getId()
        );
    }
}
