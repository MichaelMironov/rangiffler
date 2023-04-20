package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import utils.Error;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl() + "login";
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerLink = $("a[href='/register']");

    private final SelenideElement errorContainer = $(".form__error");


    @Step("Fill authorization form with credentials: username: {0}, password: {1}")
    public LoginPage fillAuthorizationForm(String login, String password) {
        enterLogin(login);
        enterPassword(password);
        return this;
    }

    @Step("Set login: {0}")
    public LoginPage enterLogin(String login) {
        usernameInput.setValue(login);
        return this;
    }

    @Step("Set password: {0}")
    public LoginPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Submit login")
    public <T extends BasePage> T submit(T expectedPage) {
        submitButton.click();
        return expectedPage;
    }

    @Step("Submit login")
    public LoginPage submit() {
        submitButton.click();
        return this;
    }

    @Step("Should appear message with text: {text}")
    public LoginPage messageShouldAppear(String text) {
        $(byText(text)).should(appear);
        return this;
    }

    @Override
    @Step("Waiting for the 'Login page' to load")
    public LoginPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }

    @Step("Expected error: {0}")
    public LoginPage errorShouldAppear(final Error badCredentials) {
        errorContainer.shouldHave(text(badCredentials.text));
        return this;
    }
}
