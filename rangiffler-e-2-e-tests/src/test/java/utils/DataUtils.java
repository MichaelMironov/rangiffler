package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import model.CountryJson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class DataUtils {

    private static final Faker faker = new Faker();

    public static String generateRandomUsername() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.bothify("????####");
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }

    public static String generateRandomSurname() {
        return faker.name().lastName();
    }

    public static String generateRandomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    public static List<String> allCountriesNamesAndCode() throws IOException {

        final List<CountryJson> allCountries =
                new ObjectMapper().readValue(new File("src/test/resources/data/countries.json"), new TypeReference<>() {
                });
        return allCountries
                .stream().map(countryJson -> countryJson.getName() + " : " + countryJson.getCode()).toList();

    }

    public static byte[] generateRandomPhoto() {

        final ClassLoader cl = DataUtils.class.getClassLoader();

        final String[] countriesPhoto = {"BlueLagoone", "Tunis", "Waterfall"};

        final String randomCountry = countriesPhoto[(int) (Math.random() * countriesPhoto.length)];

        System.out.println(randomCountry);

        try (InputStream is = cl.getResourceAsStream("data/img/" + randomCountry + ".jpg")) {

            final byte[] encode = Base64.getEncoder().encode(Objects.requireNonNull(is).readAllBytes());
            final String s = "data:image/jpeg;base64," + new String(encode, StandardCharsets.UTF_8);
            return s.getBytes(StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
