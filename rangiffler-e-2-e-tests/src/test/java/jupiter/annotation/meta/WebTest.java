package jupiter.annotation.meta;

import io.qameta.allure.junit5.AllureJunit5;
import jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({AllureJunit5.class, BrowserExtension.class})
public @interface WebTest {
}
