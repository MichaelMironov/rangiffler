package tests.web;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GeneratePhoto;
import jupiter.annotation.GenerateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import page.MainPage;

@Epic("[WEB][Frontend]: Countries | DB preconditions")
@DisplayName("[WEB][Frontend]: Countries | DB preconditions")
class CountriesTest extends BaseWebTest {

    @Test
    @AllureId("100")
    @Tags({@Tag("WEB"), @Tag("Positive")})
    @ApiLogin(user = @GenerateUser
            (
                    photo = @GeneratePhoto(country = "Canada")
            ))
    @DisplayName("WEB: Country should appear at its given coordinates")
    void countryShouldAppearAtGivenCoordinates() {

        new MainPage().open()
                .checkPhotoByCoordinates("M152.085");

    }

}
