package tests.web;


import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GeneratePhoto;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.meta.User;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.MainPage;

import java.io.IOException;

@Epic("[WEB][Frontend] Photo | DB preconditions")
@DisplayName("[WEB][Frontend] Photo | DB preconditions")
public class PhotoTest extends BaseWebTest {

    @Test
    @AllureId("600")
    @Tags({@Tag("WEB"), @Tag("DB")})
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("WEB: User can add country photo")
    @ApiLogin(user = @GenerateUser)
    public void userCanAddPhoto() throws IOException {

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
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("WEB: User can edit country photo")
    @ApiLogin(user = @GenerateUser
            (
                    photo = @GeneratePhoto(country = "United Arab Emirates", description = "holiday")
            ))
    public void userCanEditAddedPhoto(@User UserJson user) throws IOException {

        String generatedCountry = user.getPhotos().get(0).getCountryJson().getName();
        String generatedDescription = user.getPhotos().get(0).getDescription();

        String newCountry = "Democratic Republic of the Congo";
        String newDescription = "travel";

        new MainPage()
                .open()
                .waitForPageLoaded()
                .checkPhoto(generatedDescription, generatedCountry)
                .selectPhoto(generatedDescription)
                .editPhoto()
                .setCountry(newCountry).setDescription(newDescription)
                .savePhoto()
                .selectPhoto(newDescription)
                .checkPhoto(newDescription, newCountry);

    }
}
