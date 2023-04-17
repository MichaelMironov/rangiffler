package jupiter.annotation;

import jupiter.extension.ApiAuthExtension;
import jupiter.extension.ClearCookiesAndSessionExtension;
import jupiter.extension.CreateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CreateUserExtension.class, ApiAuthExtension.class, ClearCookiesAndSessionExtension.class})
public @interface ApiLogin {

    String username() default "";

    String password() default "";

    GenerateUser user() default @GenerateUser(handleAnnotation = false);
}
