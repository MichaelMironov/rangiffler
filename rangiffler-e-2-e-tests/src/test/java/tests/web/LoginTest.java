package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.meta.User;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.MainPage;
import page.RegisterPage;
import page.WelcomePage;

import static jupiter.extension.CreateUserExtension.Selector.METHOD;
import static utils.Error.BAD_CREDENTIALS;

@Epic("[WEB][Frontend]: Authorization")
@DisplayName("[WEB][Frontend]: Authorization")
class LoginTest extends BaseWebTest {

    @Test
    @AllureId("100")
    @Tags({@Tag("WEB"), @Tag("Positive")})
    @GenerateUser
    @DisplayName("WEB: Main page should be displayed after login by a new user")
    void mainPageShouldBeDisplayedAfterSuccessLogin(@User(selector = METHOD) UserJson user) {

        new WelcomePage().open()
                .toLogin()
                .fillAuthorizationForm(user.getUsername(), user.getPassword())
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("101")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @GenerateUser
    @DisplayName("WEB: User remains unauthorized if username/password is entered incorrectly")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@User(selector = METHOD) UserJson user) {

        new WelcomePage().open()
                .toLogin()
                .fillAuthorizationForm(user.getUsername(), user.getPassword() + "BAD")
                .submit(new LoginPage())
                .errorShouldAppear(BAD_CREDENTIALS);

    }

    @Test
    @AllureId("102")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @DisplayName("WEB: User remains unauthorized if username/password is not entered")
    void userShouldStayOnLoginPageAfterLoginWithoutEnteringCredentials() {

        new WelcomePage().open()
                .toLogin()
                .submit(new LoginPage())
                .shouldStayOnLoginPage();

    }

    @Test
    @AllureId("103")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @DisplayName("WEB: The user can log in when going from the register page")
    @GenerateUser
    void userCanLoginFromRedirectRegisterPage(@User(selector = METHOD) UserJson user) {

        new RegisterPage().open()
                .toLogin()
                .enterLogin(user.getUsername())
                .enterPassword(user.getPassword())
                .submit(new MainPage());

    }
}
