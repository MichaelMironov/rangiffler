package page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import utils.Error;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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

    @Override
    public LoginPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        submitButton.should(visible);
        registerLink.should(visible);
        return this;
    }

    @Step("Expected error: {0}")
    public LoginPage errorShouldAppear(final Error badCredentials) {
        errorContainer.shouldHave(text(badCredentials.content));
        return this;
    }
}
