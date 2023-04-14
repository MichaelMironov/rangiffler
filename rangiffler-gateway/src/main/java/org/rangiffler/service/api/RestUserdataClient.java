package org.rangiffler.service.api;

import jakarta.annotation.Nonnull;
import org.apache.catalina.User;
import org.rangiffler.model.FriendJson;
import org.rangiffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
public class RestUserdataClient {

    private final WebClient webClient;

    private final String userdataUri;


    @Autowired
    public RestUserdataClient(WebClient webClient,
                              @Value("${rangiffler-userdata.base-uri}") String userdataUri) {
        this.webClient = webClient;
        this.userdataUri = userdataUri;
    }


    public @Nonnull
    List<UserJson> getAllUsers(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/users").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/currentUser").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull
    List<UserJson> friends(@Nonnull String username) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/friends").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/invitations").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    public UserJson sendInvitation(@Nonnull String username,
                                   @Nonnull UserJson friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/users/invite/").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull
    UserJson removeFriend(@Nonnull String username,
                                @Nonnull UserJson friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
//        params.add("friendUsername", friend.getUsername());
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/friends/remove").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull
    UserJson acceptInvitation(@Nonnull String username,
                              @Nonnull UserJson friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/friends/submit").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull
    UserJson declineInvitation(@Nonnull String username,
                               @Nonnull FriendJson invitation) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(userdataUri + "/friends/decline").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(invitation), FriendJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull
    UserJson updateUserInfo(UserJson user) {
        return webClient.patch()
                .uri(userdataUri + "/currentUser")
                .body(Mono.just(user), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }
}
