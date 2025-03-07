package guru.qa.rococo.controller;

import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.service.UserDataClient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  private final UserDataClient userDataClient;

  @Autowired
  public UserController(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @GetMapping("/user")
  public UserJson currentUser(@AuthenticationPrincipal Jwt principal) {
    String username = principal.getClaim("sub");
    return userDataClient.currentUser(username);
  }

  @GetMapping("/all")
  public List<UserJson> allUsers(@AuthenticationPrincipal Jwt principal,
                                 @RequestParam(required = false) String searchQuery) {
    String username = principal.getClaim("sub");
    return userDataClient.allUsers(username, searchQuery);
  }

  @PostMapping("/update")
  public UserJson updateUserInfo(@AuthenticationPrincipal Jwt principal,
                                 @Valid @RequestBody UserJson user) {
    String username = principal.getClaim("sub");
    return userDataClient.updateUserInfo(user.addUsername(username));
  }
}
