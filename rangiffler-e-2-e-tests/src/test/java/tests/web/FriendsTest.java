package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.Friends;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.meta.User;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import page.MainPage;

@Epic("[WEB][Frontend]: Friends")
@DisplayName("[WEB][Frontend]: Friends")
public class FriendsTest extends BaseWebTest {

    @Test
    @AllureId("400")
    @Tag("WEB")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("WEB: The user should see a list of their friends")
    @ApiLogin(user = @GenerateUser(friends = @Friends(count = 3)))
    void shouldViewExistingFriendsInTable(@User UserJson user) {
        new MainPage().open()
                .toFriends()
                .checkExistingFriendsCount(user.getFriends().size());
    }
}
