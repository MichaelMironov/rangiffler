package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.meta.User;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.MainPage;
import page.WelcomePage;

import static jupiter.extension.CreateUserExtension.Selector.METHOD;
import static utils.DataUtils.generateRandomPassword;
import static utils.DataUtils.generateRandomUsername;
import static utils.Error.PASSWORDS_SHOULD_BE_EQUAL;

@Epic("[WEB][Frontend]: Registration")
@DisplayName("[WEB][Frontend]: Registration")
public class RegistrationTest extends BaseWebTest {

    @Test
    @AllureId("500")
    @Severity(SeverityLevel.BLOCKER)
    @Tags({@Tag("WEB"), @Tag("Positive")})
    @DisplayName("WEB: The user can successfully register")
    void userSuccessfullyRegister() {
        String username = generateRandomUsername();
        String password = generateRandomPassword();

        new WelcomePage().open()
                .toRegistration()
                .setUsername(username)
                .setPassword(password)
                .confirmPassword(password)
                .submitRegistration()
                .toLogin()
                .waitForPageLoaded()
                .fillAuthorizationForm(username, password)
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("501")
    @Severity(SeverityLevel.CRITICAL)
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
                .submitRegistration()
                .errorShouldAppear("Username `" + username + "` already exists");
    }

    @Test
    @AllureId("502")
    @Severity(SeverityLevel.CRITICAL)
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
                .submitRegistration()
                .errorShouldAppear(PASSWORDS_SHOULD_BE_EQUAL.text);
    }

}
