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
}