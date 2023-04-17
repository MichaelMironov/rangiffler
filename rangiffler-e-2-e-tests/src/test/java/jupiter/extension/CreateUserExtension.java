package jupiter.extension;

import api.AuthClient;
import api.UserdataClient;
import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.Friends;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Invitations;
import jupiter.annotation.meta.User;
import model.FriendJson;
import model.UserJson;
import org.junit.jupiter.api.extension.*;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static utils.DataUtils.generateRandomPassword;
import static utils.DataUtils.generateRandomUsername;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final UserdataClient userdataClient = new UserdataClient();
    private final AuthClient authClient = new AuthClient();

    public static final ExtensionContext.Namespace
            ON_METHOD_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.METHOD),
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.NESTED);


    @Step("API creating user")
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {

        final String testId = getTestId(context);
        Map<Selector, GenerateUser> userAnnotation = extractGenerateUserAnnotations(context);

        for (Map.Entry<Selector, GenerateUser> generateUserEntry : userAnnotation.entrySet()) {

            GenerateUser generateUser = generateUserEntry.getValue();
            String username = generateUser.username();
            String password = generateUser.password();

            if (username.isEmpty()) username = generateRandomUsername();
            if (password.isEmpty()) password = generateRandomPassword();

            UserJson userJson = registerUser(username, password);

            createFriendsIfPresent(generateUser, userJson);
            createInvitationsIfPresent(generateUser, userJson);

            context.getStore(generateUserEntry.getKey().getNamespace()).put(testId, userJson);
        }
    }

    private void createFriendsIfPresent(final GenerateUser generateUser, final UserJson user) throws Exception {

        final Friends friends = generateUser.friends();

        if (friends.handleAnnotation() && friends.count() > 0) {
            for (int i = 0; i < friends.count(); i++) {

                UserJson friend = registerUser(generateRandomUsername(), generateRandomPassword());

                FriendJson addFriend = new FriendJson();
                FriendJson invitation = new FriendJson();

                addFriend.setUsername(friend.getUsername());
                invitation.setUsername(user.getUsername());

                userdataClient.addFriend(user.getUsername(), addFriend);
                userdataClient.acceptInvitation(friend.getUsername(), invitation);

                user.getFriends().add(friend);
            }
        }
    }

    private void createInvitationsIfPresent(final GenerateUser generateUser, final UserJson user) throws Exception {

        final Invitations invitations = generateUser.invitations();

        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {

                UserJson invitation = registerUser(generateRandomUsername(), generateRandomPassword());

                FriendJson addFriend = new FriendJson();
                addFriend.setUsername(user.getUsername());

                userdataClient.addFriend(invitation.getUsername(), addFriend);

                user.getInvitations().add(invitation);
            }
        }
    }

    private UserJson registerUser(final String username, final String password) throws Exception {
        authClient.authorize();
        final Response<Void> response = authClient.register(username, password);
        if (response.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
        UserJson currentUser = userdataClient.getCurrentUser(username);
        currentUser.setPassword(password);
        return currentUser;
    }

    private Map<Selector, GenerateUser> extractGenerateUserAnnotations(final ExtensionContext context) {

        Map<Selector, GenerateUser> annotationOnTest = new HashMap<>();

        final GenerateUser annotationOnMethod = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (annotationOnMethod != null && annotationOnMethod.handleAnnotation())
            annotationOnTest.put(Selector.METHOD, annotationOnMethod);

        final ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.user().handleAnnotation())
            annotationOnTest.put(Selector.NESTED, apiLoginAnnotation.user());

        return annotationOnTest;
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
               && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        final User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.selector().getNamespace()).get(testId, UserJson.class);
    }

    public enum Selector {
        METHOD, NESTED;

        public ExtensionContext.Namespace getNamespace() {
            switch (this) {
                case METHOD -> {
                    return ON_METHOD_USERS_NAMESPACE;
                }

                case NESTED -> {
                    return API_LOGIN_USERS_NAMESPACE;
                }

                default -> throw new IllegalStateException();
            }
        }
    }

    private String getTestId(final ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }
}
