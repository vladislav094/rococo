package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.auth.AuthAuthorityEntity;
import guru.qa.rococo.data.entity.auth.AuthUserEntity;
import guru.qa.rococo.data.entity.auth.Authority;
import guru.qa.rococo.data.entity.userdata.UserEntity;
import guru.qa.rococo.data.repository.AuthUserRepository;
import guru.qa.rococo.data.repository.UserdataUserRepository;
import guru.qa.rococo.data.repository.implRepository.auth.AuthUserRepositoryHibernate;
import guru.qa.rococo.data.repository.implRepository.userdata.UserdataUserRepositoryHibernate;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.rest.TestData;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.service.UserdataClieint;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class UserdataDbClient implements UserdataClieint {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    // USERDATA DB, USER TABLE
    @Override
    @Nonnull
    @Step("Create user using SQL")
    public UserJson createUser(@Nonnull String username, @Nonnull String password) {
        return requireNonNull(
                xaTxTemplate.execute(
                        () -> {
                            AuthUserEntity authUser = authUserEntity(username, password);
                            authUserRepository.create(authUser);
                            UserJson user = UserJson.fromEntity(userdataUserRepository.create(userEntity(username)));
                            return user.addTestData(new TestData(password));
                        }
                )
        );
    }

    @Nullable
    public UserJson getUserByName(@Nonnull String name) {
        Optional<UserEntity> user = userdataUserRepository.findByUsername(name);
        return UserJson.fromEntity(user.orElseThrow());
    }

    @Nullable
    public UserJson getUserById(@Nonnull String id) {
        Optional<UserEntity> user = userdataUserRepository.findById(UUID.fromString(id));
        return UserJson.fromEntity(user.orElseThrow());
    }

    @Nonnull
    public UserJson updateUser(@Nonnull UserJson user) {
        return requireNonNull(xaTxTemplate.execute(() -> {
            UserEntity userEntity = UserEntity.fromJson(user);
            return UserJson.fromEntity(userdataUserRepository.update(userEntity));
        }));
    }

    public void deleteUdUser(@Nonnull UserJson user) {
        xaTxTemplate.execute(() -> {
            userdataUserRepository.remove(UserEntity.fromJson(user));
            return null;
        });
    }

    private UserEntity targetUserEntity(UserJson user) {
        return userdataUserRepository.findById(user.id())
                .orElseThrow();
    }

    private AuthUserEntity randomAuthUserEntity() {
        String username = randomUsername();
        AuthUserEntity authUser = authUserEntity(username, "1234");
        return authUserRepository.create(authUser);
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        a -> {
                            AuthAuthorityEntity ae = new AuthAuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(a);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}
