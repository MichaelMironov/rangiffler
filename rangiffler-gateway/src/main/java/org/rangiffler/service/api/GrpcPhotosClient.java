package org.rangiffler.service.api;

import com.google.protobuf.Empty;
import guru.qa.grpc.rangiffler.grpc.Country;
import guru.qa.grpc.rangiffler.grpc.Photo;
import guru.qa.grpc.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.rangiffler.model.CountryJson;
import org.rangiffler.model.PhotoJson;
import org.rangiffler.model.UserJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GrpcPhotosClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcPhotosClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcPhotosClient")
    private RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub photoServiceBlockingStub;
    private final RestUserdataClient restUserdataClient;

    @Autowired
    public GrpcPhotosClient(RestUserdataClient restUserdataClient) {
        this.restUserdataClient = restUserdataClient;
    }

    public @Nonnull
    List<PhotoJson> getUserPhotos(@Nonnull String username) {

        try {
            return photoServiceBlockingStub.getPhotosForUser(EMPTY).getPhotosList()
                    .stream().filter(photo -> photo.getUsername().equals(username))
                    .map(PhotoJson::fromGrpcMessage).toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull
    List<PhotoJson> getFriendPhotos(@Nonnull String username) {

        List<UserJson> friends = restUserdataClient.friends(username);

        try {
            List<PhotoJson> photos = photoServiceBlockingStub.getPhotosForFriend(EMPTY).getPhotosList()
                    .stream().filter(photo -> !photo.getUsername().equals(username))
                    .map(PhotoJson::fromGrpcMessage).toList();

            List<String> friendsNames = friends.stream().map(UserJson::getUsername).toList();

            return photos.stream().filter(p -> friendsNames.contains(p.getUsername())).collect(Collectors.toList());

        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull
    PhotoJson addUserPhoto(@Nonnull String username, PhotoJson photoJson) {
        LOG.info("Accepted JSON-photo: \n username: {}\n country: {} \ndescription: {}",
                photoJson.getUsername(), photoJson.getCountryJson(), photoJson.getDescription());
        CountryJson countryJson = photoJson.getCountryJson();
        try {
            return PhotoJson.fromGrpcMessage(
                    photoServiceBlockingStub.addPhoto(
                            Photo.newBuilder()
                                    .setPhoto(photoJson.getPhoto())
                                    .setCountry(Country.newBuilder()
                                            .setUuid(countryJson.getId().toString())
                                            .setCode(countryJson.getCode())
                                            .setName(countryJson.getName()))
                                    .setDescription(photoJson.getDescription())
                                    .setUsername(username)
                                    .build()));
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull
    PhotoJson editUserPhoto(@Nonnull String username, PhotoJson photoJson) {
        CountryJson countryJson = photoJson.getCountryJson();
        try {
            return PhotoJson.fromGrpcMessage(
                    photoServiceBlockingStub.editPhoto(
                            Photo.newBuilder()
                                    .setId(photoJson.getId().toString())
                                    .setCountry(Country.newBuilder()
                                            .setUuid(countryJson.getId().toString())
                                            .setCode(countryJson.getCode())
                                            .setName(countryJson.getName()))
                                    .setDescription(photoJson.getDescription())
                                    .setUsername(username)
                                    .build()));

        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public Empty deletePhoto(UUID photoId) {
        try {
            return photoServiceBlockingStub.deletePhoto(
                    Photo.newBuilder()
                            .setId(photoId.toString())
                            .build()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
