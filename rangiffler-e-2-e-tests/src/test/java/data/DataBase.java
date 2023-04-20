package data;

import config.Config;
import org.apache.commons.lang3.StringUtils;

public enum DataBase {
    USERDATA("jdbc:postgresql://%s/rangiffler-userdata"),
    AUTH("jdbc:postgresql://%s/rangiffler-auth"),
    PHOTO("jdbc:postgresql://%s/rangiffler-photo"),
    GEO("jdbc:postgresql://%s/rangiffler-geo");
    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    private static final Config CFG = Config.getConfig();

    public String getUrl() {
        final String url = String.format(this.url, CFG.databaseAddress());
        System.out.println(url);
        return url;
    }

    public String getUrlForP6Spy() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(getUrl(), "jdbc:");
    }
}
