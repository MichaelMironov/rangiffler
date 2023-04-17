package tests.rest;

import api.AuthClient;
import jupiter.annotation.meta.RestTest;

@RestTest
public abstract class BaseRestTest {
    protected static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
}
