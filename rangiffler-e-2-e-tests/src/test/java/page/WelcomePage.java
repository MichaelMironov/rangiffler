package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {

    public static final String URL = CFG.frontUrl();
    private final SelenideElement loginButton = $(byText("Login"));
    private final SelenideElement registerButton = $(byText("Register"));

    @Step("Click on button [Login]")
    public LoginPage toLogin() {
        loginButton.click();
        return new LoginPage();
    }

    @Override
    public WelcomePage waitForPageLoaded() {
        loginButton.should(visible);
        registerButton.should(visible);
        return this;
    }

    @Step("Open 'Welcome page'")
    public WelcomePage open() {
        return Selenide.open(URL, WelcomePage.class);
    }

}
