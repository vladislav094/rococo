package guru.qa.rococo.data.dao.implDao.userdata;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.userdata.UserdataUserDao;
import guru.qa.rococo.data.entity.userdata.UserEntity;
import guru.qa.rococo.data.mapper.UserdataUserEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.data.jdbc.DataSources.dataSource;

public class UserdataUserDaoSpringJdbc implements UserdataUserDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();

    @Override
    public UserEntity create(UserEntity user) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(url));
//        KeyHolder kh = new GeneratedKeyHolder();
//        jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(
//                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
//                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
//                    Statement.RETURN_GENERATED_KEYS
//            );
//            ps.setString(1, user.getUsername());
//            ps.setString(2, user.getCurrency().name());
//            ps.setString(3, user.getFirstname());
//            ps.setString(4, user.getSurname());
//            ps.setBytes(5, user.getPhoto());
//            ps.setBytes(6, user.getPhotoSmall());
//            ps.setString(7, user.getFullname());
//            return ps;
//        }, kh);
//
//        final UUID generatedKey = (UUID) kh.getKeys().get("id");
//        user.setId(generatedKey);
        return user;
    }

    @Override
    public UserEntity update(UserEntity user) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(url));
//        jdbcTemplate.update("""
//                        UPDATE "user"
//                            SET currency = ?,
//                              firstname = ?,
//                              surname = ?,
//                              photo = ?,
//                              photo_small = ?,
//                              full_name = ?
//                            WHERE id = ?
//                        """,
//                user.getCurrency().name(),
//                user.getFirstname(),
//                user.getFullname(),
//                user.getPhoto(),
//                user.getPhotoSmall(),
//                user.getFullname(),
//                user.getId());
//
//        jdbcTemplate.batchUpdate("""
//
//                                                INSERT INTO friendship (requester_id, addressee_id, status)
//                        VALUES (?, ?, ?)
//                        ON CONFLICT (requester_id, addressee_id)
//                        DO UPDATE SET status = ?
//                        """,
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setObject(1, user.getId());
//                        ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
//                        ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
//                        ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return user.getFriendshipRequests().size();
//                    }
//                });
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(url));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserdataUserEntityRowMapper.instance,
                        id
                ));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(
                new JdbcTemplate(dataSource(url)).queryForObject(
                        "SELECT * FROM \"user\" WHERE username = ?",
                        UserdataUserEntityRowMapper.instance,
                        username
                ));
    }

    @Override
    public List<UserEntity> findAll() {
        return new JdbcTemplate(dataSource(url)).query(
                "SELECT * FROM \"user\"",
                UserdataUserEntityRowMapper.instance
        );
    }

    @Override
    public void remove(UserEntity user) {
        new JdbcTemplate(dataSource(url)).update(
                "DELETE FROM \"user\" WHERE id = ?",
                user.getId()
        );
    }
}
