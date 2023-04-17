package page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import page.component.module.FriendsModule;
import page.component.module.ProfileModule;

import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {
    public Header() {
        super($(byTagName("header")));
    }

    private final SelenideElement addPhotoButton = self.$(byText("Add photo"));

    private final SelenideElement profileIcon = self.$(".MuiAvatar-circular");
    private final SelenideElement friendsIcon = self.$("div[aria-label='Your friends']");

    public Header addPhoto() {
        addPhotoButton.click();
        return this;
    }

    @Step("Go to user profile")
    public ProfileModule toProfile() {
        profileIcon.click();
        return new ProfileModule();
    }

    @Step("Go to user friends")
    public FriendsModule toFriends() {
        friendsIcon.click();
        return new FriendsModule();
    }

}
