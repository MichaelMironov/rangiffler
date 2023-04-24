package page.component.module;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsModule {

    private final SelenideElement friendsTable = $("table[aria-label='friends table']");

    @Step("Checking the expected number of friends in size: {0}")
    public FriendsModule checkExistingFriendsCount(final int expectedSize) {
        friendsTable.$$(byTagName("tr"))
                .shouldHave(size(expectedSize));
        return this;
    }

    @Step("Remove friend by name {0}")
    public FriendsModule removeFriendByName(String name) {

        $$(byTagName("tr"))
                .findBy(text(name))
                .find("button[aria-label='Remove friend']")
                .click();
        $("button[type='submit']").should(appear).click();
        return this;
    }
}
