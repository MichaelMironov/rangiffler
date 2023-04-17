package jupiter.extension;

public class JpaExtension implements AroundAllTestsExtension{
    @Override
    public void afterAllTests() {
        AroundAllTestsExtension.super.afterAllTests();
    }
}
