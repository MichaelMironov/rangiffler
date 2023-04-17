package config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3001/";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8080/";
    }

    @Override
    public String userdataUrl() {
        return "http://127.0.0.1:8089/";
    }

    @Override
    public String geoGrpcUrl() {
        return "127.0.0.1";
    }

    @Override
    public int geoGrpcPort() {
        return 8092;
    }

    @Override
    public String photoGrpcUrl() {
        return "127.0.0.1";
    }

    @Override
    public int photoGrpcPort() {
        return 8094;
    }

    @Override
    public String databaseAddress() {
        return "127.0.0.1:5432";
    }
}
