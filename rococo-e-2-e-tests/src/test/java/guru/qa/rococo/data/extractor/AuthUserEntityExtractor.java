package guru.qa.rococo.data.extractor;

import guru.qa.rococo.data.entity.auth.AuthAuthorityEntity;
import guru.qa.rococo.data.entity.auth.AuthUserEntity;
import guru.qa.rococo.data.entity.auth.Authority;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserEntityExtractor implements ResultSetExtractor<AuthUserEntity> {

    public static final AuthUserEntityExtractor instance = new AuthUserEntityExtractor();

    private AuthUserEntityExtractor() {

    }

    /**
     "SELECT a.id as authority_id," +
     " authority," +
     " user_id as id," +
     " u.username," +
     " u.password," +
     " u.enabled," +
     " u.account_non_expired," +
     " u.account_non_locked," +
     " u.credentials_non_expired " +
     "FROM \"user\" u join public.authority a on u.id = a.user_id WHERE u.id = ?"
     */

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, uuid -> {
                        AuthUserEntity result = new AuthUserEntity();
                        try {
                            result.setId(rs.getObject("id", UUID.class));
                            result.setUsername(rs.getString("username"));
                            result.setPassword(rs.getString("password"));
                            result.setEnabled(rs.getBoolean("enabled"));
                            result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                            result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                            result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return result;
                    }
            );

            AuthAuthorityEntity authority = new AuthAuthorityEntity();
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            authority.setUser(user);
            user.getAuthorities().add(authority);
        }
        return userMap.get(userId);
    }
}
