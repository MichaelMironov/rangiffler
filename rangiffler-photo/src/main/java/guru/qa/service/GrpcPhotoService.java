package guru.qa.service;

import com.google.protobuf.Empty;
import guru.qa.data.CountryEntity;
import guru.qa.data.PhotoEntity;
import guru.qa.data.repository.CountryRepository;
import guru.qa.data.repository.PhotosRepository;
import guru.qa.grpc.rangiffler.grpc.Country;
import guru.qa.grpc.rangiffler.grpc.Photo;
import guru.qa.grpc.rangiffler.grpc.Photos;
import guru.qa.grpc.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class GrpcPhotoService extends RangifflerPhotoServiceGrpc.RangifflerPhotoServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger("*** GRPC server ***");
    private final CountryRepository countryRepository;
    private final PhotosRepository photosRepository;

    @Override
    public void getPhotosForUser(Empty request, StreamObserver<Photos> responseObserver) {

        List<PhotoEntity> photos = photosRepository.findAll();

        Photos response = Photos.newBuilder()
                .addAllPhotos(photos.stream().map(photo ->
                        {
                            CountryEntity country = countryRepository.findById(photo.getCountryId())
                                    .orElseThrow(() -> new RuntimeException("Не найдена страна для фото"));
                            LOG.info("GRPC.getPhotosForUser. Founded country in repo: {}", country);
                            return Photo.newBuilder()
                                    .setId(photo.getId().toString())
                                    .setPhoto(new String(photo.getPhoto(), StandardCharsets.UTF_8))
                                    .setCountry(Country.newBuilder()
                                            .setUuid(country.getId().toString())
                                            .setCode(country.getCode())
                                            .setName(country.getName())
                                            .build())
                                    .setDescription(photo.getDescription())
                                    .setUsername(photo.getUsername())
                                    .build();
                        })
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void getPhotosForFriend(Empty request, StreamObserver<Photos> responseObserver) {

        List<PhotoEntity> photos = photosRepository.findAll();

        Photos response = Photos.newBuilder()
                .addAllPhotos(photos.stream().map(photo ->
                        {
                            CountryEntity country = countryRepository.findById(photo.getCountryId())
                                    .orElseThrow(() -> new RuntimeException("Не найдена страна для фото"));
                            return Photo.newBuilder()
                                    .setId(photo.getId().toString())
                                    .setPhoto(new String(photo.getPhoto(), StandardCharsets.UTF_8))
                                    .setCountry(Country.newBuilder()
                                            .setUuid(country.getId().toString())
                                            .setCode(country.getCode())
                                            .setName(country.getName())
                                            .build())
                                    .setDescription(photo.getDescription())
                                    .setUsername(photo.getUsername())
                                    .build();
                        })
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void addPhoto(Photo requestPhoto, StreamObserver<Photo> responseObserver) {

        Country requestCountry = requestPhoto.getCountry();

        CountryEntity countryEntity = countryRepository.findByCode(requestCountry.getCode());

        PhotoEntity photoEntity = PhotoEntity.builder()
                .countryId(countryEntity.getId())
                .photo(requestPhoto.getPhoto().getBytes(StandardCharsets.UTF_8))
                .description(requestPhoto.getDescription())
                .username(requestPhoto.getUsername())
                .build();

        PhotoEntity saved = photosRepository.save(photoEntity);

        Photo response = Photo.newBuilder()
                .setId(saved.getId().toString())
                .setPhoto(new String(photoEntity.getPhoto(), StandardCharsets.UTF_8))
                .setCountry(Country.newBuilder()
                        .setUuid(countryEntity.getId().toString())
                        .setCode(requestCountry.getCode())
                        .setName(requestCountry.getName())
                        .build()
                )
                .setDescription(photoEntity.getDescription())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void editPhoto(Photo requestPhoto, StreamObserver<Photo> responseObserver) {

        Country requestCountry = requestPhoto.getCountry();

        CountryEntity country = countryRepository.findByCode(requestCountry.getCode());

        PhotoEntity editablePhoto = photosRepository.findById(UUID.fromString(requestPhoto.getId())).orElseThrow(() -> new RuntimeException("Фото не найдено в БД"));

        editablePhoto.setCountryId(country.getId());
        editablePhoto.setDescription(requestPhoto.getDescription());

        PhotoEntity saved = photosRepository.save(editablePhoto);

        Photo response = Photo.newBuilder()
                .setId(saved.getId().toString())
                .setPhoto(new String(saved.getPhoto(), StandardCharsets.UTF_8))
                .setCountry(Country.newBuilder()
                        .setUuid(country.getId().toString())
                        .setCode(country.getCode())
                        .setName(country.getName())
                        .build())
                .setDescription(saved.getDescription())
                .setUsername(saved.getUsername())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePhoto(Photo request, StreamObserver<Empty> responseObserver) {

        PhotoEntity deletablePhoto = photosRepository.findById(UUID.fromString(request.getId())).orElseThrow(() -> new RuntimeException("Фото не найдено в БД"));

        photosRepository.delete(deletablePhoto);

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
}
