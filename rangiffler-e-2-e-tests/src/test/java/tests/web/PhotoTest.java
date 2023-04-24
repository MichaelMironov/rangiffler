package tests.web;


import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.Friends;
import jupiter.annotation.GeneratePhoto;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.meta.User;
import model.PhotoJson;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.MainPage;

import java.io.IOException;

import static jupiter.extension.CreateUserExtension.Selector.NESTED;

@SuppressWarnings("ALL")
@Epic("[WEB][Frontend] Photo | DB preconditions")
@DisplayName("[WEB][Frontend] Photo | DB preconditions")
class PhotoTest extends BaseWebTest {

    @Test
    @AllureId("600")
    @Tags({@Tag("WEB"), @Tag("DB")})
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("WEB: User can add country photo")
    @ApiLogin(user = @GenerateUser)
    void userCanAddPhoto() throws IOException {

        String photo = "data/img/Tunis.jpg";

        new MainPage().open()
                .waitForPageLoaded()
                .getHeader().addPhoto(photo)
                .setPhotoDescription("travel")
                .selectCountry("Kosovo")
                .savePhoto()
                .checkPhoto("travel", "Kosovo", photo);

    }

    @Test
    @AllureId("601")
    @Tags({@Tag("WEB"), @Tag("DB")})
    @DisplayName("WEB: User can edit country photo")
    @ApiLogin(user = @GenerateUser
            (
                    photo = @GeneratePhoto(country = "United Arab Emirates", description = "holiday")
            ))
    void userCanEditAddedPhoto(@User(selector = NESTED) UserJson user) throws IOException {

        final PhotoJson generatedPhoto = user.getPhotos().get(0);

        String newCountry = "Democratic Republic of the Congo";
        String newDescription = "travel";

        new MainPage()
                .open()
                .waitForPageLoaded()
                .checkPhoto(generatedPhoto.getDescription(), generatedPhoto.getCountryJson().getName())
                .selectPhoto(generatedPhoto.getDescription())
                .editPhoto()
                .setCountry(newCountry).setDescription(newDescription)
                .savePhoto()
                .selectPhoto(newDescription)
                .checkPhoto(newDescription, newCountry);

    }

    @Test
    @AllureId("602")
    @Tags({@Tag("WEB"), @Tag("DB")})
    @DisplayName("WEB: User can remove country photo")
    @ApiLogin(user = @GenerateUser
            (
                    photo = @GeneratePhoto(country = "Democratic Republic of the Congo", description = "holiday")
            ))
    void userCanRemoveAddedPhoto(@User(selector = NESTED) UserJson user) throws IOException {

        final PhotoJson generatedPhoto = user.getPhotos().get(0);

        new MainPage()
                .open()
                .waitForPageLoaded()
                .selectPhoto(generatedPhoto.getDescription())
                .removePhoto()
                .photoCountShouldBeEqual(0);

    }

    @Test
    @AllureId("603")
    @Tags({@Tag("WEB"), @Tag("DB")})
    @DisplayName("WEB: User can see friend country photo")
    @ApiLogin(user = @GenerateUser(
            friends = @Friends(count = 1,
                    photo = @GeneratePhoto(country = "Canada", description = "travel")
            )))
    void userCanSeeFriendCountryPhoto(@User(selector = NESTED) UserJson user) throws IOException {

        new MainPage()
                .open()
                .waitForPageLoaded()
                .toFriendsTravels()
                .checkPhoto("travel", "Canada");

    }

}
