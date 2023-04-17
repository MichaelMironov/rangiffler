package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import page.component.Header;
import page.component.module.FriendsModule;
import page.component.module.ProfileModule;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final Header header = new Header();

    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().shouldBe(Condition.visible);
        return this;
    }

    @Step("Open 'Main page'")
    public MainPage open() {
        return Selenide.open(URL, MainPage.class);
    }

    public ProfileModule toProfile() {
        return header.toProfile();
    }

    public FriendsModule toFriends() {
        return header.toFriends();
    }
}
