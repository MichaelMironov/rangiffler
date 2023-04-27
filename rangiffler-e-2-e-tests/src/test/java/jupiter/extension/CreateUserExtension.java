package jupiter.extension;

import api.AuthClient;
import api.UserdataClient;
import data.dao.PostgresHibernateAuthDAO;
import data.dao.PostgresHibernateCountriesDAO;
import data.dao.PostgresHibernateUsersDAO;
import data.dao.PostgresSpringJdbcPhotosDAO;
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

import java.io.IOException;
import java.util.*;

import static io.qameta.allure.Allure.step;
import static java.util.Optional.ofNullable;
import static model.CountryJson.fromEntity;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static utils.DataUtils.*;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    private final UserdataClient userdataClient = new UserdataClient();
    private final PostgresHibernateAuthDAO authDAO = new PostgresHibernateAuthDAO();
    private final PostgresHibernateUsersDAO usersDAO = new PostgresHibernateUsersDAO();
    private final AuthClient authClient = new AuthClient();
    private final PostgresSpringJdbcPhotosDAO photosSpringDAO = new PostgresSpringJdbcPhotosDAO();
    private final PostgresHibernateCountriesDAO hibernateCountriesDAO = new PostgresHibernateCountriesDAO();

    public static final Namespace
            ON_METHOD_USERS_NAMESPACE = Namespace.create(CreateUserExtension.class, Selector.METHOD),
            API_LOGIN_USERS_NAMESPACE = Namespace.create(CreateUserExtension.class, Selector.NESTED);


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

            UserJson userJson = new UserJson();
            userJson.setUsername(username);
            userJson.setPassword(password);

            if (generateUser.register()) userJson = registerUser(username, password);

            createFriendsIfPresent(generateUser, userJson);
            createInvitationsIfPresent(generateUser, userJson);
            createPhotoIfPresent(generateUser, userJson);
            addAvatarIfPresent(generateUser, userJson);

            context.getStore(generateUserEntry.getKey().getNamespace()).put(testId, userJson);
        }
    }

    private void addAvatarIfPresent(final GenerateUser generateUser, final UserJson userJson) throws IOException {
        final GenerateAvatar avatar = generateUser.avatar();

        if (avatar.handleAnnotation()) {
            step("API generate avatar for user", () -> {
                userJson.setAvatar(generateAvatar());
                userdataClient.updateUser(userJson);
            });
        }
    }

    private void createPhotoIfPresent(final GenerateUser generateUser, final UserJson userJson) {

        final GeneratePhoto[] photos = generateUser.photo();

        if (generateUser.handleAnnotation() && photos.length > 0) {
            for (final GeneratePhoto photo : photos) {
                step("[Spring-Jdbc|Photos] generate country photo for user: " + userJson.getUsername(),
                        () -> generatePhoto(userJson, photo));
            }
        }
    }

    private void generatePhoto(final UserJson userJson, final GeneratePhoto photo) {

        final CountryEntity country = hibernateCountriesDAO.getCountryByName(photo.country());

        final CountryJson countryJson = fromEntity(country);

        String randomPhoto = DataUtils.generateRandomPhoto();
        String description = photo.description();
        if (description.isEmpty()) description = generateRandomSentence(5);

        PhotoJson photoJson = new PhotoJson();
        photoJson.setCountryJson(countryJson);
        photoJson.setUsername(userJson.getUsername());
        photoJson.setDescription(description);
        photoJson.setPhoto(randomPhoto);

        userJson.setPhotos(List.of(photoJson));

        photosSpringDAO.addPhoto(PhotoJson.toEntity(photoJson));
    }

    private void createFriendsIfPresent(final GenerateUser generateUser, final UserJson user) throws Exception {

        final Friends friends = generateUser.friends();
        final GeneratePhoto[] generatePhotos = generateUser.friends().photo();

        if (friends.handleAnnotation() && friends.count() > 0) {

            step("API creating user friend", () -> {
                for (int i = 0; i < friends.count(); i++) {

                    UserJson friend = registerUser(generateRandomUsername(), generateRandomPassword());

                    FriendJson addFriend = new FriendJson();
                    FriendJson invitation = new FriendJson();

                    addFriend.setUsername(friend.getUsername());
                    invitation.setUsername(user.getUsername());

                    userdataClient.addFriend(user.getUsername(), addFriend);
                    userdataClient.acceptInvitation(friend.getUsername(), invitation);

                    user.getFriends().add(friend);

                    if (generatePhotos.length > 0) {
                        for (final GeneratePhoto photo : generatePhotos) {
                            generatePhoto(friend, photo);
                        }
                    }
                }
            });
        }
    }

    private void createInvitationsIfPresent(final GenerateUser generateUser, final UserJson user) throws Exception {

        final Invitations invitations = generateUser.invitations();

        if (invitations.handleAnnotation() && invitations.count() > 0) {
            step("API creating invitations", () -> {
                for (int i = 0; i < invitations.count(); i++) {

                    UserJson invitation = registerUser(generateRandomUsername(), generateRandomPassword());

                    FriendJson addFriend = new FriendJson();
                    addFriend.setUsername(user.getUsername());

                    userdataClient.addFriend(invitation.getUsername(), addFriend);

                    user.getInvitations().add(invitation);
                }
            });
        }
    }

    private UserJson registerUser(final String username, final String password) throws Exception {
        authClient.authorize();
        final Response<Void> response = authClient.register(username, password);
        if (response.code() != 201) {
            throw new RuntimeException("User already registered");
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

    @Override
    @Step("Clearing test data")
    public void afterTestExecution(final ExtensionContext context) throws Exception {
        final String testId = getTestId(context);

        final Optional<UserJson> nestedUser = ofNullable(context.getStore(API_LOGIN_USERS_NAMESPACE).get(testId, UserJson.class));
        final Optional<UserJson> methodUser = ofNullable(context.getStore(ON_METHOD_USERS_NAMESPACE).get(testId, UserJson.class));

        if (nestedUser.isPresent()) {

            final UserJson nUser = nestedUser.get();

            nUser.getFriends().forEach(friend -> usersDAO.removeByUsername(friend.getUsername()));
            nUser.getFriends().forEach(friend -> authDAO.removeByUsername(friend.getUsername()));
            nUser.getFriends().forEach(friend -> photosSpringDAO.removePhotoByUsername(friend.getUsername()));

            photosSpringDAO.removePhotoByUsername(nUser.getUsername());
            usersDAO.removeByUsername(nUser.getUsername());
            authDAO.removeByUsername(nUser.getUsername());
        }

        if (methodUser.isPresent()) {

            final UserJson mUser = methodUser.get();

            mUser.getFriends().forEach(friend -> usersDAO.removeByUsername(friend.getUsername()));
            mUser.getFriends().forEach(friend -> authDAO.removeByUsername(friend.getUsername()));
            mUser.getFriends().forEach(friend -> photosSpringDAO.removePhotoByUsername(friend.getUsername()));

            photosSpringDAO.removePhotoByUsername(mUser.getUsername());
            usersDAO.removeByUsername(mUser.getUsername());
            authDAO.removeByUsername(mUser.getUsername());
        }
    }

    public enum Selector {
        METHOD, NESTED;

        public Namespace getNamespace() {
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
