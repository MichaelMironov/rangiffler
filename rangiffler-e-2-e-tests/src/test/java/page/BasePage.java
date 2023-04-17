package page;

import com.codeborne.selenide.Selenide;
import config.Config;
import io.qameta.allure.Step;

public abstract class BasePage<T extends BasePage> {
    protected static final Config CFG = Config.getConfig();

    public abstract T waitForPageLoaded();

//    public abstract <P> P openPage(Class<P> cls);

}
