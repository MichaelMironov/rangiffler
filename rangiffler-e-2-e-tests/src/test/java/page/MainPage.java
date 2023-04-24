package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import condition.PhotoCondition;
import io.qameta.allure.Step;
import model.FriendStatus;
import org.openqa.selenium.Keys;
import page.component.Header;
import page.component.module.FriendsModule;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static model.FriendStatus.FRIEND;
import static model.FriendStatus.NOT_FRIEND;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final Header header = new Header();
    private final SelenideElement textareaDescription = $$(byTagName("textarea")).get(0);

    private final SelenideElement peopleTable = $("table[aria-label='all people table']");

    private final ElementsCollection listPhoto = $$(".MuiImageListItem-root");

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

    @Step("Check photo by coordinates: {}")
    public MainPage checkPhotoByCoordinates(String coordinates) {
        $x("//*[contains(@d, '" + coordinates + "')]").should(appear);
        return this;
    }

    @Step("Click on button [Logout]")
    public WelcomePage logout() {
        $("svg[data-testid='LogoutIcon']").click();
        return new WelcomePage();
    }

    @Step("To people")
    public MainPage toPeopleAround() {
        $(withTagAndText("button", "People Around")).click();
        return this;
    }

    @Step("To friends travels")
    public MainPage toFriendsTravels() {
        $(withTagAndText("button", "Friends travels")).click();
        return this;
    }

    @Step("Add to friend by username")
    public MainPage addToFriendUserByUsername(String username) {
        peopleTable.$$(byTagName("td"))
                .findBy(text(username)).parent()
                .find(byTagName("button")).should(appear).hover().click();
        return this;
    }

    @Step("Check the message about the successful sending of a friend request")
    public MainPage statusShouldBe(FriendStatus status, String username) {

        final SelenideElement user = peopleTable.$$(byTagName("td"))
                .findBy(text(username)).parent();

        if (status.equals(NOT_FRIEND)) {
            user.find(byTagName("button"))
                    .shouldBe(attribute("aria-label", "Add friend"));

        } else if (status.equals(FRIEND)) {
            user.find(byTagName("button"))
                    .shouldBe(attribute("aria-label", "Remove friend"));

        } else user.shouldHave(text(status.text), Duration.ofSeconds(10L));
        return this;
    }

    @Step("Check compliance photo after adding: {1}, {0}")
    public MainPage checkPhoto(String description, String country, String resource) {

        final SelenideElement addedPhoto = $$(".photo__list-item")
                .findBy(attribute("alt", description));

        addedPhoto.shouldHave(PhotoCondition.photo(resource));
        $(byText(country)).shouldBe(visible);
        return this;
    }

    @Step("Check compliance photo after editing: {1}, {0}")
    public MainPage checkPhoto(String description, String country) {

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

    @Step("Click on button [Remove photo]")
    public MainPage removePhoto() {
        $("svg[data-testid='DeleteOutlineIcon']").click();
        $(byTagAndText("button", "Delete")).click();
        return this;
    }

    @Step("Set country: {0}")
    public MainPage setCountry(String country) {
        $("div[role='button']").click();
        $$("li[role='option']").findBy(text(country)).click();
        return this;
    }

    @Step("The number of photos must be - {0}")
    public MainPage photoCountShouldBeEqual(int count) {
        listPhoto.shouldHave(size(count));
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

    @Step("Check friend request from user by name - {0}")
    public MainPage checkFriendRequestFromUser(final String username) {

        final SelenideElement foundUserRow = peopleTable.$$(byTagName("tr"))
                .findBy(text(username));

        foundUserRow
                .find(byTagName("button"))
                .shouldHave(attribute("aria-label", "Accept invitation"), Duration.ofSeconds(10L));

        //TODO: Decline not such element
//        foundUserRow
//                .find(byTagName("button"))
//                .shouldHave(attribute("aria-label", "Decline invitation"));

        return this;
    }

    @Step("Reject a friend request from a user with username: {0}")
    public MainPage declineInvitation(final String username) {

        final SelenideElement foundUserRow = peopleTable.$$(byTagName("tr"))
                .findBy(text(username));

        foundUserRow.find("button[aria-label='Decline invitation']")
                .hover().click();

        $(byTagAndText("button", "Decline"))
                .scrollIntoView(false).click();

        return this;
    }

    @Step("Accept a friend request from a user with username: {0}")
    public MainPage acceptInvitation(final String username) {

        final SelenideElement foundUserRow = peopleTable.$$(byTagName("tr"))
                .findBy(text(username));

        foundUserRow.find("button[aria-label='Accept invitation']")
                .scrollIntoView(false).click();

        return this;
    }

    @Step("Refresh page")
    public MainPage refreshPage() {
        Selenide.refresh();
        return this;
    }
}
