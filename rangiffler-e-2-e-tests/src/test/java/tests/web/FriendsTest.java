package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.Friends;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Invitations;
import jupiter.annotation.meta.User;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import page.MainPage;
import page.WelcomePage;

import static jupiter.extension.CreateUserExtension.Selector.METHOD;
import static jupiter.extension.CreateUserExtension.Selector.NESTED;
import static model.FriendStatus.*;

@Epic("[WEB][Frontend]: Friends")
@DisplayName("[WEB][Frontend]: Friends")
class FriendsTest extends BaseWebTest {

    @Test
    @AllureId("400")
    @Tag("WEB")
    @DisplayName("WEB: The user should see a list of their friends")
    @ApiLogin(user = @GenerateUser(friends = @Friends(count = 3)))
    void shouldViewExistingFriendsInTable(@User UserJson user) {
        new MainPage().open()
                .getHeader()
                .toFriends()
                .checkExistingFriendsCount(user.getFriends().size());
    }

    @Test
    @AllureId("401")
    @Tag("WEB")
    @DisplayName("WEB: The user must receive a friend request")
    @ApiLogin(user = @GenerateUser(username = "friend_test", password = "123"))
    @GenerateUser(username = "friend_test1", password = "123")
    void userCanAddFriend(@User(selector = METHOD) UserJson user) {

        new MainPage().open()
                .toPeopleAround()
                .addToFriendUserByUsername(user.getUsername())
                .statusShouldBe(INVITATION_SENT, user.getUsername())
                .logout();

        new WelcomePage()
                .toLogin()
                .enterLogin(user.getUsername())
                .enterPassword(user.getPassword())
                .submit(new MainPage())
                .toPeopleAround()
                .checkFriendRequestFromUser("friend_test");
    }

    @Test
    @AllureId("402")
    @Tag("WEB")
    @DisplayName("WEB: The user delete friend")
    @ApiLogin(user = @GenerateUser(friends = @Friends(count = 1)))
    void userCanDeleteFriend(@User UserJson user) {

        final UserJson friend = user.getFriends().get(0);

        new MainPage().open()
                .getHeader()
                .toFriends()
                .removeFriendByName(friend.getUsername())
                .checkExistingFriendsCount(0);

    }

    @Test
    @AllureId("403")
    @Tag("WEB")
    @DisplayName("WEB: The user can decline friend request")
    @ApiLogin(user =
    @GenerateUser(invitations = @Invitations(count = 1)))
    void userCanDeclineFriendsRequest(@User(selector = NESTED) UserJson user) {

        final String invitationUsername = user.getInvitations().get(0).getUsername();

        new MainPage().open()
                .toPeopleAround()
                .checkFriendRequestFromUser(invitationUsername)
                .declineInvitation(invitationUsername)
                .statusShouldBe(NOT_FRIEND, invitationUsername);
    }

    @Test
    @AllureId("404")
    @Tag("WEB")
    @DisplayName("WEB: The user can accept friend request")
    @ApiLogin(
            user = @GenerateUser(invitations = @Invitations(count = 1))
    )
    void userCanAcceptFriendsRequest(@User(selector = NESTED) UserJson user) {

        final String invitationUsername = user.getInvitations().get(0).getUsername();

        new MainPage().open()
                .toPeopleAround()
                .checkFriendRequestFromUser(invitationUsername)
                .acceptInvitation(invitationUsername)
                .statusShouldBe(FRIEND, invitationUsername);
    }
}
