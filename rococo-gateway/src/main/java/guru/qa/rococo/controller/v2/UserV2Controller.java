package guru.qa.rococo.controller.v2;

import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.service.UserDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v2/users")
public class UserV2Controller {

  private static final Logger LOG = LoggerFactory.getLogger(UserV2Controller.class);

  private final UserDataClient userDataClient;

  @Autowired
  public UserV2Controller(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }


  @GetMapping("/all")
  public Page<UserJson> allUsers(@AuthenticationPrincipal Jwt principal,
                                 @PageableDefault Pageable pageable,
                                 @RequestParam(required = false) String searchQuery) {
    String username = principal.getClaim("sub");
    return userDataClient.allUsers(username, pageable, searchQuery);
  }
}
