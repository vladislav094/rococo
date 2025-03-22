package guru.qa.rococo.controller;

import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.service.api.GrpcUserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final GrpcUserClient grpcUserClient;

    @Autowired
    public UserController(GrpcUserClient grpcUserClient) {
        this.grpcUserClient = grpcUserClient;
    }

    @GetMapping
    public UserJson currentUser(@AuthenticationPrincipal Jwt principal) {
        final String username = principal.getClaim("sub");
        return grpcUserClient.currentUser(username);
    }

    @PatchMapping
    public UserJson updateUser(@AuthenticationPrincipal Jwt principal, @RequestBody UserJson user) {
        final String username = principal.getClaim("sub");
        return grpcUserClient.updateUserInfo(user.addUsername(username));
    }
}
