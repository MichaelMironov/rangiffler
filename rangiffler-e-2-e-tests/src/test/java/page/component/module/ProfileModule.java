package page.component.module;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import page.MainPage;
import page.component.Header;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

public class ProfileModule extends Header {

    private final SelenideElement firstnameInput = $("input[name='firstName'");
    private final SelenideElement lastnameInput = $("input[name='lastName'");
    private final SelenideElement submitButton = $("button[type='submit']");

    @Step("Set firstname: {0}")
    public ProfileModule setFirstname(String firstname) {
        firstnameInput.setValue(firstname);
        return this;
    }

    @Step("Set lastname: {0}")
    public ProfileModule setLastname(String lastname) {
        lastnameInput.setValue(lastname);
        return this;
    }

    @Step("Click [Save] button")
    public MainPage clickSave() {
        submitButton.click();
        return new MainPage();
    }

    @Step("Check name: {0}")
    public ProfileModule firstnameShouldBe(String firstname) {
        firstnameInput.shouldHave(value(firstname));
        return this;
    }

    @Step("Check surname: {0}")
    public ProfileModule lastnameShouldBe(String surname) {
        lastnameInput.shouldHave(value(surname));
        return this;
    }
}
