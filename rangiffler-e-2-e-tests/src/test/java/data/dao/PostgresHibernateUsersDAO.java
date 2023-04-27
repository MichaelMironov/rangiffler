package data.dao;

import data.DataBase;
import data.entity.UserEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import io.qameta.allure.Step;


public class PostgresHibernateUsersDAO extends JpaService implements UsersDAO {

    public PostgresHibernateUsersDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.USERDATA).createEntityManager());
    }

    @Step("[Hibernate] Add user to database")
    @Override
    public int addUser(UserEntity users) {
        persist(users);
        return 0;
    }

    @Step("[Hibernate] Update user in database")
    @Override
    public void updateUser(UserEntity user) {
        merge(user);
    }

    @Override
    public void remove(UserEntity user) {
        delete(user);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return em.createQuery("select u from UserEntity u where u.username=:username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    @Step("[Hibernate|Userdata] Remove user with name - {0}")
    public void removeByUsername(final String username) {
        final UserEntity user = getByUsername(username);
        delete(user);
    }
}
