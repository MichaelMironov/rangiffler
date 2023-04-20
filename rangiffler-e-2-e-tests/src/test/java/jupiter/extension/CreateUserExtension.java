package jupiter.extension;

import api.AuthClient;
import api.UserdataClient;
import data.dao.PostgresHibernateCountriesDAO;
import data.dao.PostgresHibernatePhotosDAO;
import data.entity.CountryEntity;
import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import jupiter.annotation.*;
import jupiter.annotation.meta.User;
import model.CountryJson;
import model.FriendJson;
import model.PhotoJson;
import model.UserJson;
import org.junit.jupiter.api.extension.*;
import retrofit2.Response;
import utils.DataUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static utils.DataUtils.*;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final UserdataClient userdataClient = new UserdataClient();
    private final AuthClient authClient = new AuthClient();

    private final PostgresHibernatePhotosDAO hibernatePhotosDAO = new PostgresHibernatePhotosDAO();
    private final PostgresHibernateCountriesDAO hibernateCountriesDAO = new PostgresHibernateCountriesDAO();

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
            createPhotoIfPresent(generateUser, userJson);

            context.getStore(generateUserEntry.getKey().getNamespace()).put(testId, userJson);
        }
    }

    @Step("[BD][Hibernate] Generating country photo")
    private void createPhotoIfPresent(final GenerateUser generateUser, final UserJson userJson) {

        final GeneratePhoto[] photos = generateUser.photo();
        final String username = userJson.getUsername();

        if (generateUser.handleAnnotation()) {
            for (final GeneratePhoto photo : photos) {

                final CountryEntity country = hibernateCountriesDAO.getCountryByName(photo.country());

                final CountryJson countryJson = CountryJson.fromEntity(country);

                PhotoJson photoJson = new PhotoJson();
                photoJson.setCountryJson(countryJson);
                photoJson.setUsername(username);
                photoJson.setDescription(photo.description());

                byte[] randomPhoto = DataUtils.generateRandomPhoto();
                String description = photo.description();
                if (description.isEmpty()) description = generateRandomSentence(5);

                userJson.setPhotos(List.of(photoJson));

                hibernatePhotosDAO.addPhotoByUsername(username, country, description, randomPhoto);
            }
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
