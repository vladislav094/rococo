package guru.qa.rococo.service.api;

import guru.qa.rococo.ex.NoRestResponseException;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.model.page.RestPage;
import guru.qa.rococo.service.UserDataClient;
import guru.qa.rococo.service.utils.HttpQueryPaginationAndSort;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "rococo-userdata", name = "client", havingValue = "rest")
public class RestUserDataClient implements UserDataClient {

  private final RestTemplate restTemplate;
  private final String rococoUserdataApiUri;

  @Autowired
  public RestUserDataClient(RestTemplate restTemplate,
                            @Value("${rococo-userdata.base-uri}") String rococoUserdataBaseUri) {
    this.restTemplate = restTemplate;
    this.rococoUserdataApiUri = rococoUserdataBaseUri + "/internal";
  }

  @Nonnull
  @Override
  public UserJson currentUser(@Nonnull String username) {
    return Optional.ofNullable(
        restTemplate.getForObject(
            rococoUserdataApiUri + "/users/current?username={username}",
            UserJson.class,
            username
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/users/current/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson updateUserInfo(@Nonnull UserJson user) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            rococoUserdataApiUri + "/users/update",
            user,
            UserJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/users/update/ Route]"));
  }

  @Nonnull
  @Override
  public List<UserJson> allUsers(@Nonnull String username, @Nullable String searchQuery) {
    return Arrays.asList(
        Optional.ofNullable(
            restTemplate.getForObject(
                rococoUserdataApiUri + "/users/all?username={username}&searchQuery={searchQuery}",
                UserJson[].class,
                username,
                searchQuery
            )
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson[] response is given [/users/all/ Route]"))
    );
  }

  @SuppressWarnings("unchecked")
  @Nonnull
  @Override
  public Page<UserJson> allUsers(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
    ResponseEntity<RestPage<UserJson>> response = restTemplate.exchange(
        rococoUserdataApiUri + "/v2/users/all?username={username}&searchQuery={searchQuery}"
        + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestPage<UserJson>>() {
        },
        username,
        searchQuery
    );
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/users/all/ Route]"));
  }

  @Nonnull
  @Override
  public List<UserJson> friends(@Nonnull String username, @Nullable String searchQuery) {
    return Arrays.asList(
        Optional.ofNullable(
            restTemplate.getForObject(
                rococoUserdataApiUri + "/friends/all?username={username}&searchQuery={searchQuery}",
                UserJson[].class,
                username,
                searchQuery
            )
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson[] response is given [/friends/all/ Route]"))
    );
  }

  @SuppressWarnings("unchecked")
  @Nonnull
  @Override
  public Page<UserJson> friends(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
    ResponseEntity<RestPage<UserJson>> response = restTemplate.exchange(
        rococoUserdataApiUri + "/v2/friends/all?username={username}&searchQuery={searchQuery}"
        + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestPage<UserJson>>() {
        },
        username,
        searchQuery
    );
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/friends/all/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            rococoUserdataApiUri + "/invitations/send?username={username}&targetUsername={targetUsername}",
            null,
            UserJson.class,
            username,
            targetUsername
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/invitations/send/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            rococoUserdataApiUri + "/invitations/accept?username={username}&targetUsername={targetUsername}",
            null,
            UserJson.class,
            username,
            targetUsername
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/invitations/accept/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            rococoUserdataApiUri + "/invitations/decline?username={username}&targetUsername={targetUsername}",
            null,
            UserJson.class,
            username,
            targetUsername
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/invitations/decline/ Route]"));
  }

  @Override
  public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) {
    restTemplate.delete(
        rococoUserdataApiUri + "/friends/remove?username={username}&targetUsername={targetUsername}",
        username,
        targetUsername
    );
  }
}
