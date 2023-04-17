package utils;

public enum Error {

    BAD_CREDENTIALS("Неверные учетные данные пользователя"),
    PASSWORDS_SHOULD_BE_EQUAL("Passwords should be equal");

    public final String content;

    Error(String content) {
        this.content = content;
    }
}
