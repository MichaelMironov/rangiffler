package api;

import api.service.RestService;
import io.qameta.allure.Step;
import model.FriendJson;
import model.UserJson;

import java.io.IOException;
import java.util.List;

public class UserdataClient extends RestService {

    public UserdataClient() {
        super(CFG.userdataUrl());
    }

    private final UserdataApi userdataApi = retrofit.create(UserdataApi.class);

    @Step("GET('/currentUser') request to userdata service")
    public UserJson getCurrentUser(final String username) throws IOException {

        return userdataApi.currentUser(username)
                .execute()
                .body();
    }

    @Step("PATCH('/currentUser') request to userdata service for update user info")
    public UserJson updateUser(final UserJson userJson) throws IOException {

        return userdataApi.updateUserInfo(userJson)
                .execute()
                .body();
    }

    @Step("GET('/users') request to userdata service")
    public List<UserJson> allUsers(final String username) throws IOException {

        return userdataApi.allUsers(username)
                .execute()
                .body();
    }

    @Step("POST('/users/invite/') request to userdata service")
    public UserJson addFriend(final String username,
                              final FriendJson friend) throws IOException {

        return userdataApi.addFriend(username, friend)
                .execute()
                .body();
    }

    @Step("POST('/friends/submit') request to userdata service for accept friend invitation")
    public UserJson acceptInvitation(final String username,
                                           final FriendJson invitation) throws IOException {

        return userdataApi.acceptInvitation(username, invitation)
                .execute()
                .body();
    }
}
