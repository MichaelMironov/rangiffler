package data.dao;

import data.DataBase;
import data.entity.auth.AuthEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

public class PostgresHibernateAuthDAO extends JpaService {
    public PostgresHibernateAuthDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.AUTH).createEntityManager());
    }

    @Step("[Hibernate|Auth] Remove user by name - {0}")
    public void removeByUsername(@Nonnull String username) {
        final AuthEntity userAuthEntity = getUserAuthEntity(username);
        delete(userAuthEntity);
    }

    public AuthEntity getUserAuthEntity(@Nonnull String username) {
        return em.createQuery("select u from AuthEntity u where u.username=:username", AuthEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
