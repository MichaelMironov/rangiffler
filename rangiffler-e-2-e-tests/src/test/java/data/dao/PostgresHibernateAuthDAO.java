package data.dao;

import data.DataBase;
import data.entity.auth.AuthEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import io.qameta.allure.Step;

public class PostgresHibernateAuthDAO extends JpaService {
    public PostgresHibernateAuthDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.AUTH).createEntityManager());
    }

    @Step("[Hibernate] Remove user from database")
    public void removeByUsername(AuthEntity authEntity) {
        delete(authEntity);
    }

    public AuthEntity getUserAuthEntity(String username) {
        return em.createQuery("select u from AuthEntity u where u.username=:username", AuthEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
