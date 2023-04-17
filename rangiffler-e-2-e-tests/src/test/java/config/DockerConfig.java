package config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

    static {
        Configuration.browserSize = "1920x1200";
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.timeout = 10000;
    }

    @Override
    public String authUrl() {
        return null;
    }

    @Override
    public String frontUrl() {
        return null;
    }

    @Override
    public String gatewayUrl() {
        return null;
    }

    @Override
    public String userdataUrl() {
        return null;
    }

    @Override
    public String geoGrpcUrl() {
        return null;
    }

    @Override
    public int geoGrpcPort() {
        return 0;
    }

    @Override
    public String photoGrpcUrl() {
        return null;
    }

    @Override
    public int photoGrpcPort() {
        return 0;
    }

    @Override
    public String databaseAddress() {
        return "rangiffler-all-db:5432";
    }
}
