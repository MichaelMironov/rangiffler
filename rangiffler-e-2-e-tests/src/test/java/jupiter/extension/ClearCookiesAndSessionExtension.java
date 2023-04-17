package jupiter.extension;

import api.context.CookieHolder;
import api.context.SessionStorageHolder;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ClearCookiesAndSessionExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CookieHolder.getInstance().flushAll();
        SessionStorageHolder.getInstance().flushAll();
    }
}
