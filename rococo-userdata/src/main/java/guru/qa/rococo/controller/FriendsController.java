package guru.qa.rococo.controller;

import guru.qa.rococo.model.IUserJson;
import guru.qa.rococo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/friends")
public class FriendsController {

  private static final Logger LOG = LoggerFactory.getLogger(FriendsController.class);

  private final UserService userService;

  @Autowired
  public FriendsController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/all")
  public List<? extends IUserJson> friends(@RequestParam String username,
                                           @RequestParam(required = false) String searchQuery) {
    return userService.friends(username, searchQuery);
  }

  @DeleteMapping("/remove")
  public void removeFriend(@RequestParam String username,
                           @RequestParam String targetUsername) {
    userService.removeFriend(username, targetUsername);
  }
}
