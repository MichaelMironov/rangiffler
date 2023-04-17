package jupiter.extension;

import api.AuthClient;
import api.context.CookieHolder;
import api.context.SessionStorageHolder;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import config.Config;
import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import model.UserJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.io.IOException;
import java.util.Objects;

import static jupiter.extension.CreateUserExtension.API_LOGIN_USERS_NAMESPACE;

public class ApiAuthExtension implements BeforeEachCallback {

    private final AuthClient authClient = new AuthClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace AUTH_EXTENSION_NAMESPACE
            = ExtensionContext.Namespace.create(ApiAuthExtension.class);

    @Step("API authorize user")
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        final GenerateUser generateUserAnnotation = apiLoginAnnotation.user();
        if ((!generateUserAnnotation.handleAnnotation() && apiLoginAnnotation.username().isEmpty() && apiLoginAnnotation.password().isEmpty()))
            throw new IllegalArgumentException("You have to provide in @ApiLogin annotation user by username/password or @GenerateUser");

        final String testId = getTestId(context);

        UserJson userToLogin;

        if (generateUserAnnotation.handleAnnotation())
            userToLogin = context.getStore(API_LOGIN_USERS_NAMESPACE).get(testId, UserJson.class);
        else {
            userToLogin = new UserJson();
            userToLogin.setUsername(apiLoginAnnotation.username());
            userToLogin.setPassword(apiLoginAnnotation.password());
        }

        apiLogin(userToLogin.getUsername(), userToLogin.getPassword());
        Selenide.open(CFG.frontUrl());
        final SessionStorage sessionStorage = Selenide.sessionStorage();
        sessionStorage.setItem("codeChallenge", SessionStorageHolder.getInstance().getCodeChallenge());
        sessionStorage.setItem("id_token", SessionStorageHolder.getInstance().getToken());
        sessionStorage.setItem("codeVerifier", SessionStorageHolder.getInstance().getCodeVerifier());

        WebDriverRunner.getWebDriver().manage()
                .addCookie(new Cookie("JSESSIONID", CookieHolder.getInstance().getCookieValue("JSESSIONID")));
    }

    private void apiLogin(final String username, final String password) throws IOException {
        authClient.authorize();
        authClient.login(username, password);
        final JsonNode authClientToken = authClient.getToken();
        SessionStorageHolder.getInstance().addToken(authClientToken.get("id_token").asText());
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }
}
