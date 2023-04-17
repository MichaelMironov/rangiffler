package api.interceptors;

import api.context.SessionStorageHolder;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ExtractCodeFromRespInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull final Chain chain) throws IOException {
        String code = chain.request().url().queryParameter("code");
        if (code != null) {
            SessionStorageHolder.getInstance().addCode(code);
        }
        return chain.proceed(chain.request());
    }
}
