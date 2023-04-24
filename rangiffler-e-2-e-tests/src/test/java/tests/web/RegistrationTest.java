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
import page.WelcomePage;

import static jupiter.extension.CreateUserExtension.Selector.METHOD;
import static utils.DataUtils.generateRandomPassword;
import static utils.DataUtils.generateRandomUsername;
import static utils.Error.PASSWORDS_SHOULD_BE_EQUAL;

@Epic("[WEB][Frontend]: Registration")
@DisplayName("[WEB][Frontend]: Registration")
class RegistrationTest extends BaseWebTest {

    @Test
    @AllureId("500")
    @Tags(@Tag("WEB"))
    @DisplayName("WEB: The user can successfully register")
    void userSuccessfullyRegister() {
        String username = generateRandomUsername();
        String password = generateRandomPassword();

        new WelcomePage().open()
                .toRegistration()
                .setUsername(username)
                .setPassword(password)
                .confirmPassword(password)
                .submit()
                .toLogin()
                .waitForPageLoaded()
                .fillAuthorizationForm(username, password)
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("501")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @DisplayName("WEB: Exists user can't register")
    @GenerateUser
    void existsUserCantRegister(@User(selector = METHOD) UserJson existUser) {

        final String username = existUser.getUsername();
        final String password = generateRandomPassword();

        new WelcomePage().open()
                .toRegistration()
                .setUsername(username)
                .setPassword(password)
                .confirmPassword(password)
                .submit()
                .errorShouldAppear("Username `" + username + "` already exists");
    }

    @Test
    @AllureId("502")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @DisplayName("WEB: Should show error if password and confirm password are not equal")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {

        final String username = generateRandomUsername();
        final String password = generateRandomPassword();

        new WelcomePage().open()
                .toRegistration()
                .setUsername(username)
                .setPassword(password)
                .confirmPassword(password + "BAD")
                .submit()
                .errorShouldAppear(PASSWORDS_SHOULD_BE_EQUAL.text);
    }

    @Test
    @AllureId("503")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @DisplayName("WEB: The user remains on the registration page if fields not filled")
    void shouldStayOnRegistrationPageIfFieldsNotEntered() {

        new WelcomePage().open()
                .toRegistration()
                .submit()
                .shouldStayOnRegisterPage();
    }

    @Test
    @AllureId("504")
    @Tags({@Tag("WEB"), @Tag("Negative")})
    @DisplayName("WEB: Registered user cannot register")
    @GenerateUser
    void registeredUserCannotRegister(@User(selector = METHOD) UserJson user) {

        new WelcomePage().open()
                .toRegistration()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .confirmPassword(user.getPassword())
                .submit()
                .errorShouldAppear("Username `" + user.getUsername() + "` already exists");
    }

    @Test
    @AllureId("505")
    @Tags(@Tag("WEB"))
    @DisplayName("WEB: The user can register when going from the login page")
    @GenerateUser
    void userCanRegisterFromRedirectLoginPage(@User(selector = METHOD) UserJson user) {

        new LoginPage().open()
                .waitForPageLoaded()
                .toRegister()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .confirmPassword(user.getPassword())
                .submit();

    }

}
