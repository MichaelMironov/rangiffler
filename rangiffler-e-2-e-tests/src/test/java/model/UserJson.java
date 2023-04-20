package model;


import com.fasterxml.jackson.annotation.JsonProperty;
import data.entity.PhotoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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

    @JsonProperty("password")
    private String password;

    @JsonProperty("friendStatus")
    private FriendStatus friendStatus = FriendStatus.NOT_FRIEND;

    private transient List<UserJson> friends = new ArrayList<>();
    private transient List<UserJson> invitations = new ArrayList<>();
    private transient List<PhotoJson> photos = new ArrayList<>();

}

