package guru.qa.rococo.data.dao.implDao.artist;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.dao.artist.ArtistDao;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.data.jdbc.Connections.holder;
import static guru.qa.rococo.data.jdbc.DataSources.dataSource;

public class ArtistDaoSpringJdbc implements ArtistDao {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.artistJdbcUrl();

    @NotNull
    @Override
    public ArtistEntity create(ArtistEntity artist) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(url));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO artist (name, biography, photo) " +
                                    "VALUES (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, artist.getName());
                    ps.setString(2, artist.getBiography());
                    ps.setBytes(3, artist.getPhoto());
                    return ps;
                }, kh);
        UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        artist.setId(generatedKey);

        return artist;
    }

    @Override
    public Optional<ArtistEntity> findById(@NotNull UUID id) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "SELECT * FROM \"artist\" WHERE  id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    ArtistEntity ae = new ArtistEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setName(rs.getString("name"));
                    ae.setBiography(rs.getString("biography"));
                    ae.setPhoto(rs.getBytes("photo"));

                    return Optional.of(ae);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ArtistEntity> findByName(@NotNull String name) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "SELECT * FROM \"artist\" WHERE  name = ?"
        )) {
            ps.setString(1, name);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    ArtistEntity ae = new ArtistEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setName(rs.getString("name"));
                    ae.setBiography(rs.getString("biography"));
                    ae.setPhoto(rs.getBytes("photo"));

                    return Optional.of(ae);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
