package guru.qa.rangiffler.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserEntityTest {

    UserEntity userEntity;
    UserEntity userFriendEntity;
    FriendsEntity firstFriendEntity;
    FriendsEntity secondFriendEntity;


    @BeforeEach
    void setUsers() {
        userEntity = new UserEntity();
        userEntity.setUsername("Mike");
        userEntity.setId(UUID.randomUUID());

        userFriendEntity = new UserEntity();
        userFriendEntity.setUsername("Dima");
        userFriendEntity.setId(UUID.randomUUID());

        firstFriendEntity = new FriendsEntity();
        UserEntity firstFriendUserEntity = new UserEntity();
        firstFriendUserEntity.setId(UUID.randomUUID());
        firstFriendEntity.setUser(firstFriendUserEntity);

        secondFriendEntity = new FriendsEntity();
        UserEntity secondFriendUserEntity = new UserEntity();
        secondFriendUserEntity.setId(UUID.randomUUID());
        secondFriendEntity.setUser(secondFriendUserEntity);
    }

    @Test
    void addFriends() {

        userEntity.setFriends(List.of(firstFriendEntity, secondFriendEntity));

        assertEquals(2, userEntity.getFriends().size(), "User must have 2 friends");
    }

    @Test
    void removeFriends() {

        userEntity.addFriends(false, userFriendEntity);
        userEntity.removeFriends(userFriendEntity);

        assertEquals(0, userEntity.getFriends().size(), "User should not have friends after deleting them");
    }

    @Test
    void setInvites() {

        userEntity.setInvites(List.of(firstFriendEntity, secondFriendEntity));

        final FriendsEntity firstInvite = userEntity.getInvites().get(0);
        final FriendsEntity secondInvite = userEntity.getInvites().get(1);

        assertAll(
                () -> assertEquals(firstInvite.getUser().getId(), firstFriendEntity.getUser().getId()),
                () -> assertEquals(secondInvite.getUser().getId(), secondFriendEntity.getUser().getId())
        );
    }

    @Test
    void removeInvites() {

        List<FriendsEntity> invites = new ArrayList<>();
        invites.add(firstFriendEntity);
        invites.add(secondFriendEntity);

        userEntity.setInvites(invites);
        userEntity.removeInvites(firstFriendEntity.getUser(), secondFriendEntity.getUser());

        assertEquals(0, userEntity.getInvites().size(), "User should not have invites after deleting them");

    }

    @Test
    void getFriends() {
        userEntity.addFriends(false, userFriendEntity);
        final FriendsEntity friendEntity = userEntity.getFriends().get(0);

        assertEquals("Dima", friendEntity.getFriend().getUsername());
    }

    @Test
    void getInvites() {
        userEntity.setInvites(List.of(firstFriendEntity, secondFriendEntity));

        assertEquals(2, userEntity.getInvites().size(), "User must return 2 invites");
    }

}