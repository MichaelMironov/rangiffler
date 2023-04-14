package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rangiffler.grpc.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("country")
    private CountryJson countryJson;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

    public static PhotoJson fromGrpcMessage(Photo photo) {
        return new PhotoJson(
                UUID.fromString(photo.getId()),
                CountryJson.fromGrpcMessage(photo.getCountry()),
                photo.getPhoto(),
                photo.getDescription(),
                photo.getUsername());
    }
}
