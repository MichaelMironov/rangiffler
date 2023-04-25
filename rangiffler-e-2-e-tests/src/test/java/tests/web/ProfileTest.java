package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateAvatar;
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
    @Tags(@Tag("WEB"))
    @DisplayName("WEB: The user can edit all fields in profile")
    @ApiLogin(user = @GenerateUser)
    void shouldUpdateProfileWithAllFieldsSet() {

        final String newFirstname = generateRandomName();
        final String newLastname = generateRandomSurname();

        MainPage mainPage = new MainPage()
                .open()
                .getHeader()
                .toProfile()
                .setFirstname(newFirstname)
                .setLastname(newLastname)
                .clickSave();

        mainPage.getHeader()
                .toProfile()
                .firstnameShouldBe(newFirstname)
                .lastnameShouldBe(newLastname);
    }

    @Test
    @AllureId("201")
    @Tags(@Tag("WEB"))
    @DisplayName("WEB: The user can set avatar in profile")
    @ApiLogin(user = @GenerateUser)
    void userCanSetAvatar() {

        final String avatar = "data/img/ava/pizzly.jpg";

        MainPage mainPage = new MainPage()
                .open()
                .getHeader()
                .toProfile()
                .setAvatar(avatar)
                .clickSave();


        mainPage.getHeader()
                .toProfile()
                .checkAvatar(avatar);
    }

    @Test
    @AllureId("202")
    @Tags(@Tag("WEB"))
    @DisplayName("WEB: The user can set new avatar in profile")
    @ApiLogin(user = @GenerateUser(avatar = @GenerateAvatar))
    void userCanEditAvatar() {

        final String newAvatar = "data/img/ava/dima.jpg";

        final MainPage mainPage = new MainPage()
                .open()
                .getHeader()
                .toProfile()
                .setAvatar(newAvatar)
                .clickSave();

        mainPage
                .getHeader()
                .toProfile()
                .checkAvatar(newAvatar);
    }
}
