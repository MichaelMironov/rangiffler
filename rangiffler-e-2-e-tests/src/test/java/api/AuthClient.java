package api;

import api.context.CookieHolder;
import api.context.SessionStorageHolder;
import api.interceptors.AddCookiesReqInterceptor;
import api.interceptors.ExtractCodeFromRespInterceptor;
import api.interceptors.ReceivedCookieRespInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import config.Config;
import io.qameta.allure.Step;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthClient {

    private static final Config CFG = Config.getConfig();

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .followRedirects(true)
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(new ReceivedCookieRespInterceptor())
            .addNetworkInterceptor(new AddCookiesReqInterceptor())
            .addNetworkInterceptor(new ExtractCodeFromRespInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(CFG.authUrl())
            .build();

    private final AuthApi authApi = retrofit.create(AuthApi.class);

    @Step("GET('/oauth2/authorize') request to auth-server")
    public void authorize() throws IOException {
        SessionStorageHolder.getInstance().init();
        authApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "authorized",
                SessionStorageHolder.getInstance().getCodeChallenge(),
                "S256"
        ).execute();
    }

    @Step("POST('/login') request to auth-server")
    public Response<Void> login(final String username,
                                final String password) throws IOException {
        return authApi.login(
                CookieHolder.getInstance().getCookieByName("JSESSIONID"),
                CookieHolder.getInstance().getCookieByName("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValue("XSRF-TOKEN"),
                username,
                password
        ).execute();
    }

    @Step("POST('/oauth2/token') request to auth-server")
    public JsonNode getToken() throws IOException {
        String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
        return authApi.getToken(
                basic,
                "client",
                CFG.frontUrl() + "authorized",
                "authorization_code",
                SessionStorageHolder.getInstance().getCode(),
                SessionStorageHolder.getInstance().getCodeVerifier()
        ).execute().body();
    }

    @Step("POST('/register') request to auth-server for user registration")
    public Response<Void> register(final String username, String password) throws IOException {
        return authApi.register(
                CookieHolder.getInstance().getCookieByName("JSESSIONID"),
                CookieHolder.getInstance().getCookieByName("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValue("XSRF-TOKEN"),
                username,
                password,
                password
        ).execute();
    }
}
