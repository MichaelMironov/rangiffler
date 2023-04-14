package guru.qa.rangiffler.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rangiffler.data.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("friendStatus")
    private FriendStatus friendStatus = FriendStatus.NOT_FRIEND;

    public static UserJson fromEntity(UserEntity entity) {
        UserJson usr = new UserJson();
        byte[] photo = entity.getAvatar();
        usr.setId(entity.getId());
        usr.setUsername(entity.getUsername());
        usr.setFirstName(entity.getFirstname());
        usr.setLastName(entity.getLastname());
        usr.setAvatar(photo != null && photo.length > 0 ? new String(entity.getAvatar(), StandardCharsets.UTF_8) : null);
        return usr;
    }

    public static UserJson fromEntity(UserEntity entity, FriendStatus friendState) {
        UserJson userJson = fromEntity(entity);
        userJson.setFriendStatus(friendState);
        return userJson;
    }
}

