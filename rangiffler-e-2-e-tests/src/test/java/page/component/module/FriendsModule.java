package page.component.module;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;

public class FriendsModule {

    private final SelenideElement friendsTable = $("table[aria-label='friends table']");

    @Step("Checking the expected number of friends in size: {0}")
    public FriendsModule checkExistingFriendsCount(final int expectedSize) {
        friendsTable.$$(byTagName("tr")).shouldHave(CollectionCondition.size(expectedSize));
        return this;
    }
}
