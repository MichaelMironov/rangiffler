package page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import page.MainPage;
import page.component.module.FriendsModule;
import page.component.module.ProfileModule;

import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($(byTagName("header")));
    }

    private final SelenideElement addPhotoButton = self.$(byText("Add photo"));
    private final SelenideElement profileIcon = self.$(".MuiAvatar-circular");
    private final SelenideElement friendsIcon = self.$("div[aria-label='Your friends']");

    @Step("Click on button [Add photo]")
    public Header addPhoto(String resource) {
        addPhotoButton.click();
        $("#file").uploadFromClasspath(resource);
        return this;
    }

    @Step("Click on button [Save photo]")
    public MainPage savePhoto() {
        $("button[type='submit']").click();
        return new MainPage();
    }

    @Step("Set description: {0}")
    public Header setPhotoDescription(String description) {
        $$(byTagName("textarea")).get(0).setValue(description);
        return this;
    }

    @Step("Select country: {0}")
    public Header selectCountry(String country) {
        $("div[role='button']").click();
        $(byText(country)).click();
        return this;
    }

    @Step("Go to user profile")
    public ProfileModule toProfile() {
        profileIcon.click();
        return new ProfileModule();
    }

    @Step("Set avatar from path: {0}")
    public ProfileModule setAvatar(String path) {
        $("input[type='file']").uploadFromClasspath(path);
        return new ProfileModule();
    }

    @Step("Go to user friends")
    public FriendsModule toFriends() {
        friendsIcon.click();
        return new FriendsModule();
    }

}
