package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    public static final String URL = CFG.authUrl() + "register";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmit = $("input[name='passwordSubmit']");

    @Step("Input username: {0}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Input password: {0}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Confirm password: {0}")
    public RegisterPage confirmPassword(String password) {
        passwordSubmit.setValue(password);
        return this;
    }

    @Step("Submit registration")
    public RegisterPage submitRegistration() {
        $(".form__submit").click();
        return new RegisterPage();
    }

    @Override
    @Step("Waiting for the 'Register page' to load")
    public RegisterPage waitForPageLoaded() {
        usernameInput.shouldBe(Condition.visible);
        passwordInput.shouldBe(Condition.visible);
        return this;
    }

    @Step("Go to user login")
    public LoginPage toLogin() {
        $("a[href*='redirect']").click();
        return new LoginPage();
    }

    @Step("Should appear error message with text: {0}")
    public RegisterPage errorShouldAppear(String errorText) {
        $(".form__error").shouldHave(text(errorText));
        return this;
    }
}
