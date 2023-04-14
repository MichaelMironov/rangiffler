package org.rangiffler.controller;

import lombok.RequiredArgsConstructor;
import org.rangiffler.model.PhotoJson;
import org.rangiffler.service.api.GrpcPhotosClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PhotoController {
    private final GrpcPhotosClient grpcPhotosClient;

    @GetMapping("/photos")
    public List<PhotoJson> getPhotosForUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return grpcPhotosClient.getUserPhotos(username);
    }

    @GetMapping("/friends/photos")
    public List<PhotoJson> getAllFriendsPhotos(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return grpcPhotosClient.getFriendPhotos(username);
    }

    @PostMapping("/photos")
    public PhotoJson addPhoto(@AuthenticationPrincipal Jwt principal,
                              @RequestBody PhotoJson photoJson) {
        String username = principal.getClaim("sub");
        return grpcPhotosClient.addUserPhoto(username, photoJson);
    }

    @PatchMapping("/photos/{id}")
    public PhotoJson editPhoto(@AuthenticationPrincipal Jwt principal,
                               @RequestBody PhotoJson photoJson) {
        String username = principal.getClaim("sub");
        return grpcPhotosClient.editUserPhoto(username, photoJson);
    }

    @DeleteMapping("/photos")
    public void deletePhoto(@AuthenticationPrincipal Jwt principal,
                            @RequestParam UUID photoId) {
        grpcPhotosClient.deletePhoto(photoId);
    }

}
