package jupiter.extension;

import data.dao.PostgresHibernateUsersDAO;
import data.dao.PostgresSpringJdbcUsersDAO;
import jupiter.annotation.meta.DAO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DAOResolver implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(final Object testInstance, final ExtensionContext context) throws Exception {
        final List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DAO.class))
                .peek(field -> field.setAccessible(true))
                .toList();
        for (final Field field : fields) {
            field.set(testInstance, System.getProperty("dao", "jpa").equals("jpa")
                    ? new PostgresHibernateUsersDAO()
                    : new PostgresSpringJdbcUsersDAO());
        }
    }
}
