package page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import condition.PhotoCondition;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import page.component.Header;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final Header header = new Header();
    private SelenideElement textareaDescription = $$(byTagName("textarea")).get(0);

    @Override
    @Step("Waiting for the 'Main page' to load")
    public MainPage waitForPageLoaded() {
        header.getSelf().shouldBe(visible);
        return this;
    }

    @Step("Open 'Main page'")
    public MainPage open() {
        return Selenide.open(URL, MainPage.class);
    }

    public Header getHeader() {
        return header;
    }

    @Step("Check compliance photo after adding: {1}, {0}")
    public MainPage checkPhoto(String description, String country, String resource) throws IOException {

        final SelenideElement addedPhoto = $$(".photo__list-item")
                .findBy(attribute("alt", description));

        addedPhoto.shouldHave(PhotoCondition.photo(resource));
        $(byText(country)).shouldBe(visible);
        return this;
    }

    @Step("Check compliance photo after editing: {1}, {0}")
    public MainPage checkPhoto(String description, String country) throws IOException {

        $$(".photo__list-item")
                .findBy(attribute("alt", description))
                .shouldBe(visible);

        $(byText(country)).shouldBe(visible);
        return this;
    }

    @Step("Select photo by description: {0}")
    public MainPage selectPhoto(String description) {
        $$(".photo__list-item").findBy(attribute("alt", description)).click();
        return this;
    }

    @Step("Set country: {0}")
    public MainPage setCountry(String country) {
        $("div[role='button']").click();
        $$("li[role='option']").findBy(text(country)).click();
        return this;
    }

    @Step("Set description: {0}")
    public MainPage setDescription(String description) {
        textareaDescription.click();
        final String clearDescription = Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE);
        textareaDescription.sendKeys(clearDescription);
        textareaDescription.setValue(description);
        return this;
    }

    @Step("Click to edit photo")
    public MainPage editPhoto() {
        $("svg[data-testid='EditIcon']").shouldBe(visible).click();
        return this;
    }

    @Step("Click on button [Save photo]")
    public MainPage savePhoto() {
        $("button[type='submit']").click();
        return this;
    }

}
