package guru.qa;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class RangifflerGeoApp {
    public static void main(String[] args) {
        SpringApplication.run(RangifflerGeoApp.class, args);
    }
}
