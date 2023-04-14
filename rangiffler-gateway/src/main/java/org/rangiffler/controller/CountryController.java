package org.rangiffler.controller;

import lombok.RequiredArgsConstructor;
import org.rangiffler.model.CountryJson;
import org.rangiffler.service.api.GrpcCountriesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController {
    private final GrpcCountriesClient grpcCountriesClient;

    @GetMapping("/countries")
    public List<CountryJson> getAllCountries(@AuthenticationPrincipal Jwt principal) {
        return grpcCountriesClient.getCountries();
    }

}
