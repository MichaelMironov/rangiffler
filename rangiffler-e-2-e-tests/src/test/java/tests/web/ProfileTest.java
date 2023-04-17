package tests.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.MainPage;

import static utils.DataUtils.generateRandomName;
import static utils.DataUtils.generateRandomSurname;

@Epic("[WEB][Frontend]: Profile")
@DisplayName("[WEB][Frontend]: Profile")
class ProfileTest extends BaseWebTest {

    @Test
    @AllureId("200")
    @Tags({@Tag("WEB"), @Tag("Positive")})
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("WEB: The user can edit all fields in profile")
    @ApiLogin(user = @GenerateUser)
    void shouldUpdateProfileWithAllFieldsSet() {

        final String newFirstname = generateRandomName();
        final String newLastname = generateRandomSurname();

        MainPage mainPage = new MainPage()
                .open()
                .waitForPageLoaded()
                .toProfile()
                .setFirstname(newFirstname)
                .setLastname(newLastname)
                .clickSave();

        Selenide.refresh();

        mainPage.toProfile()
                .firstnameShouldBe(newFirstname)
                .lastnameShouldBe(newLastname);
    }
}
