package org.rangiffler.controller;

import org.rangiffler.model.FriendJson;
import org.rangiffler.model.UserJson;
import org.rangiffler.service.api.RestUserdataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final RestUserdataClient restUserDataClient;

    public UserController(RestUserdataClient restUserDataClient) {
        this.restUserDataClient = restUserDataClient;
    }

    @GetMapping("/users")
    public List<UserJson> getAllUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserJson getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.currentUser(username);
    }

    @PatchMapping("/currentUser")
    public UserJson updateUserInfo(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        return restUserDataClient.updateUserInfo(user);
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.friends(username);
    }

    @GetMapping("invitations")
    public List<UserJson> invitations(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.invitations(username);
    }

    @PostMapping("users/invite/")
    public UserJson sendInvitation(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return restUserDataClient.sendInvitation(username, friend);
    }

    @PostMapping("friends/remove")
    public UserJson removeFriend(@AuthenticationPrincipal Jwt principal,
                                       @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return restUserDataClient.removeFriend(username, friend);
    }

    @PostMapping("friends/submit")
    public UserJson acceptFriend(@AuthenticationPrincipal Jwt principal,
                                 @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return restUserDataClient.acceptInvitation(username, friend);
    }

    @PostMapping("friends/decline")
    public UserJson declineInvitation(@AuthenticationPrincipal Jwt principal,
                                      @Validated @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return restUserDataClient.declineInvitation(username, invitation);
    }

}
