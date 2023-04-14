package guru.qa.rangiffler.controller;

import guru.qa.rangiffler.model.FriendJson;
import guru.qa.rangiffler.model.UserJson;
import guru.qa.rangiffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserDataService userService;

    @Autowired
    public UserController(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserJson> allUsers(@RequestParam String username) {
        return userService.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@RequestParam String username) {
        return userService.getCurrentUserOrCreateIfAbsent(username);
    }

    @PatchMapping("/currentUser")
    public UserJson updateUserInfo(@RequestBody UserJson user) {
        return userService.update(user);
    }

    @GetMapping("/friends")
    public List<UserJson> getFriendsByUsername(@RequestParam String username) {
        return userService.friends(username);
    }

    @GetMapping("invitations")
    public List<UserJson> invitations(@RequestParam String username) {
        return userService.invitations(username);
    }

    @PostMapping("users/invite/")
    public UserJson sendInvitation(@RequestParam String username,
                                   @RequestBody UserJson friend) {
        return userService.sendInvitation(username, friend);
    }

    @PostMapping("friends/remove")
    public UserJson removeFriend(@RequestParam String username,
                                 @RequestBody UserJson friend) {
        return userService.removeFriend(username, friend);
    }

    @PostMapping("friends/submit")
    public UserJson acceptFriend(@RequestParam String username,
                                 @RequestBody UserJson friend) {
        return userService.acceptInvitation(username, friend);
    }

    @PostMapping("friends/decline")
    public UserJson declineInvitation(@RequestParam String username,
                                      @RequestBody FriendJson invitation) {
        return userService.declineInvitation(username, invitation);
    }

}
