package tests.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.rangiffler.grpc.Countries;
import guru.qa.grpc.rangiffler.grpc.Country;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.DataUtils;

import java.io.IOException;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("[gRPC][Geo]: Countries")
@DisplayName("[gRPC][Geo]: Countries")
public class CountriesTest extends BaseGrpcTest {

    @Test
    @AllureId("700")
    @Tag("gRPC")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("gRPC: Geo service should return 175 countries")
    void getAllCountries() {

        final Countries allCountries = step("Get all countries",
                () -> serviceBlockingStub.getCountries(Empty.getDefaultInstance()));
        final List<Country> countriesList = allCountries.getAllCountriesList();

        step("Checking that 175 countries are returned in the geo service response",
                () -> assertEquals(175, countriesList.size()));

    }

    @Test
    @AllureId("701")
    @Tag("gRPC")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("gRPC: Country compliance check")
    void countryComplianceCheck() throws IOException {

        final List<Country> responseCountries = step("Get all countries",
                () -> serviceBlockingStub.getCountries(Empty.getDefaultInstance())).getAllCountriesList();

        final List<String> actualCountryCodeNames = responseCountries
                .stream().map(country -> country.getName() + " : " + country.getCode()).toList();

        step("Checking compliance of countries by name and code",
                () -> assertEquals(DataUtils.allCountriesNamesAndCode(), actualCountryCodeNames));

    }

}
