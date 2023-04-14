package guru.qa.rangiffler.service;

import guru.qa.rangiffler.data.FriendsEntity;
import guru.qa.rangiffler.data.UserEntity;
import guru.qa.rangiffler.data.repository.UserRepository;
import guru.qa.rangiffler.model.FriendJson;
import guru.qa.rangiffler.model.FriendStatus;
import guru.qa.rangiffler.model.UserJson;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class UserDataService {
    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nonnull
    UserJson getCurrentUserOrCreateIfAbsent(@Nonnull String username) {
        UserEntity userDataEntity = userRepository.findByUsername(username);
        if (userDataEntity == null) {
            userDataEntity = new UserEntity();
            userDataEntity.setUsername(username);
            return UserJson.fromEntity(userRepository.save(userDataEntity));
        } else {
            return UserJson.fromEntity(userDataEntity);
        }
    }

    public @Nonnull List<UserJson> getAllUsers(@Nonnull String username) {

        Map<UUID, UserJson> result = new HashMap<>();
        for (UserEntity user : userRepository.findByUsernameNot(username)) {
            List<FriendsEntity> sendInvites = user.getFriends();
            List<FriendsEntity> receivedInvites = user.getInvites();

            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
                        .filter(i -> i.getFriend().getUsername().equals(username))
                        .findFirst();

                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
                        .filter(i -> i.getUser().getUsername().equals(username))
                        .findFirst();

                if (inviteToMe.isPresent()) {
                    FriendsEntity invite = inviteToMe.get();
                    result.put(user.getId(), UserJson.fromEntity(user, invite.isPending()
                            ? FriendStatus.INVITATION_RECEIVED
                            : FriendStatus.FRIEND));
                }
                if (inviteFromMe.isPresent()) {
                    FriendsEntity invite = inviteFromMe.get();
                    result.put(user.getId(), UserJson.fromEntity(user, invite.isPending()
                            ? FriendStatus.INVITATION_SENT
                            : FriendStatus.FRIEND));
                }
            }

            if (!result.containsKey(user.getId())) {
                result.put(user.getId(), UserJson.fromEntity(user));
            }
        }
        return new ArrayList<>(result.values());
    }


    public @Nonnull
    UserJson update(@Nonnull UserJson user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());
        userEntity.setFirstname(user.getFirstName());
        userEntity.setLastname(user.getLastName());
        userEntity.setAvatar(user.getAvatar() != null ? user.getAvatar().getBytes(StandardCharsets.UTF_8) : null);
        UserEntity saved = userRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }

    public @Nonnull
    List<UserJson> friends(@Nonnull String username) {
        return userRepository.findByUsername(username)
                .getFriends()
                .stream()
                .filter(fe -> !fe.isPending())
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendStatus.INVITATION_SENT
                        : FriendStatus.FRIEND))
                .toList();
    }

    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        return userRepository.findByUsername(username)
                .getInvites()
                .stream()
                .filter(FriendsEntity::isPending)
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendStatus.INVITATION_RECEIVED))
                .toList();
    }

    public UserJson sendInvitation(@Nonnull String username,
                                   @Nonnull UserJson friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        currentUser.addFriends(true, userRepository.findByUsername(friend.getUsername()));
        userRepository.save(currentUser);
        friend.setFriendStatus(FriendStatus.INVITATION_SENT);
        return friend;
    }

    public @Nonnull
    UserJson acceptInvitation(@Nonnull String username,
                              @Nonnull UserJson friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity inviteUser = userRepository.findByUsername(friend.getUsername());

        FriendsEntity invite = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().equals(inviteUser))
                .findFirst()
                .orElseThrow();

        invite.setPending(false);
        currentUser.addFriends(false, inviteUser);
        UserJson acceptedFriend = UserJson.fromEntity(inviteUser, FriendStatus.FRIEND);
        userRepository.save(currentUser);
        return acceptedFriend;

    }

    public @Nonnull
    UserJson removeFriend(@Nonnull String username, @Nonnull UserJson friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToRemove = userRepository.findByUsername(friend.getUsername());
        currentUser.removeFriends(friendToRemove);
        currentUser.removeInvites(friendToRemove);
        userRepository.save(currentUser);
        return UserJson.fromEntity(friendToRemove, FriendStatus.NOT_FRIEND);
//        return currentUser
//                .getFriends()
//                .stream()
//                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
//                        ? FriendStatus.INVITATION_SENT
//                        : FriendStatus.FRIEND))
//                .toList();
    }

    public @Nonnull
    UserJson declineInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToDecline = userRepository.findByUsername(invitation.getUsername());
        currentUser.removeInvites(friendToDecline);
        currentUser.removeFriends(friendToDecline);
        userRepository.save(currentUser);
        return UserJson.fromEntity(friendToDecline, FriendStatus.NOT_FRIEND);
    }
}
