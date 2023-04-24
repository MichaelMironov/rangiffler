package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.MainPage;

@Epic("[WEB][Frontend]: Logout")
@DisplayName("[WEB][Frontend]: Logout")
public class LogoutTest extends BaseWebTest {

    @Test
    @AllureId("800")
    @Tags({@Tag("WEB"), @Tag("Positive")})
    @ApiLogin(user = @GenerateUser)
    @DisplayName("WEB: The user can log out")
    void userCanLogout() {

        new MainPage().open()
                .waitForPageLoaded()
                .logout()
                .waitForPageLoaded();
    }
}
