package config;

public interface Config {

    static Config getConfig() {

        if (System.getProperty("test.env").equals("docker"))
            return new DockerConfig();

        else if (System.getProperty("test.env").equals("local"))
            return new LocalConfig();

        else throw new IllegalArgumentException("Incorrect environment");
    }

    String authUrl();

    String frontUrl();

    String gatewayUrl();

    String userdataUrl();

    String geoGrpcUrl();

    int geoGrpcPort();

    String photoGrpcUrl();

    int photoGrpcPort();

    String databaseAddress();
}
