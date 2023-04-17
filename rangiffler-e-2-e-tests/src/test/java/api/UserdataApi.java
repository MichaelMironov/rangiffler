package api;

import model.FriendJson;
import model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserdataApi {

    @GET("/currentUser")
    Call<UserJson> currentUser(@Query("username") final String username);

    @PATCH("/currentUser")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @GET("/users")
    Call<List<UserJson>> allUsers(@Query("username") final String username);

    @POST("/users/invite/")
    Call<UserJson> addFriend(@Query("username") final String username, @Body FriendJson friend);

    @POST("/friends/submit")
    Call<UserJson> acceptInvitation(@Query("username") final String username, @Body FriendJson invitation);
}
