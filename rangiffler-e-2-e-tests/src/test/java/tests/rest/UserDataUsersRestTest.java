package tests.rest;

import api.UserdataClient;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.meta.User;
import model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.*;

@Epic("[REST][Userdata]: Users")
@DisplayName("[REST][Userdata]: Users")
public class UserDataUsersRestTest extends BaseRestTest {
    private final UserdataClient userdataClient = new UserdataClient();

    @Test
    @AllureId("300")
    @Tag("REST")
    @Severity(SeverityLevel.CRITICAL)
    @GenerateUser()
    @DisplayName("REST: For a new user, information from Userdata service should be returned with default values")
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final UserJson currentUserResponse = userdataClient.getCurrentUser(user.getUsername());

        step("Check that response contains ID (GUID)", () ->
                assertTrue(currentUserResponse.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), currentUserResponse.getUsername())
        );

    }

    @Test
    @AllureId("301")
    @Tag("REST")
    @Severity(SeverityLevel.CRITICAL)
    @GenerateUser()
    @DisplayName("REST: When updating a user, the values in Userdata service must be saved")
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String firstname = "Cors";
        final String lastname = "Customizer";

        UserJson jsonUser = new UserJson();
        jsonUser.setUsername(user.getUsername());
        jsonUser.setFirstName(firstname);
        jsonUser.setLastName(lastname);

        final UserJson updateUserInfoResponse = userdataClient.updateUser(jsonUser);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(updateUserInfoResponse.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), updateUserInfoResponse.getUsername())
        );
        step("Check that response contains updated firstname (Cors)", () ->
                assertEquals(firstname, updateUserInfoResponse.getFirstName())
        );
        step("Check that response contains updated lastname (Customizer)", () ->
                assertEquals(lastname, updateUserInfoResponse.getLastName())
        );
    }

    @Test
    @DisplayName("REST: List of all system users must not be empty")
    @AllureId("303")
    @Tag("REST")
    @Severity(SeverityLevel.CRITICAL)
    @GenerateUser()
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> allUsersResponse = userdataClient.allUsers(user.getUsername());

        step("Check that all users list is not empty", () ->
                assertFalse(allUsersResponse.isEmpty())
        );
    }
}
