package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.rangiffler.grpc.Country;
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
public class CountryJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    public static CountryJson fromGrpcMessage(Country country) {
        return new CountryJson(UUID.fromString(country.getUuid()), country.getCode(), country.getName());
    }

    public static Country toGrpcMessage(CountryJson countryJson) {
        return Country.newBuilder()
                .setUuid(countryJson.getId().toString())
                .setCode(countryJson.getCode())
                .setName(countryJson.getName())
                .build();
    }
}
