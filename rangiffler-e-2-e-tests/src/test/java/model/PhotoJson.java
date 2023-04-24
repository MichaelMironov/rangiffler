package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import data.entity.PhotoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
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

    public static PhotoEntity toEntity(PhotoJson photoJson) {
        final PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setPhoto(photoJson.getPhoto().getBytes(StandardCharsets.UTF_8));
        photoEntity.setUsername(photoJson.getUsername());
        photoEntity.setDescription(photoJson.getDescription());
        photoEntity.setCountryId(photoJson.getCountryJson().getId());
        return photoEntity;
    }

}
