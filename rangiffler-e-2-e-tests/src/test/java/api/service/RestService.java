package api.service;


import config.Config;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestService {

    protected static final Config CFG = Config.getConfig();

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new AllureOkHttp3())
            .build();

    protected final Retrofit retrofit;

    protected RestService(String restServiceUrl) {
        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(restServiceUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
