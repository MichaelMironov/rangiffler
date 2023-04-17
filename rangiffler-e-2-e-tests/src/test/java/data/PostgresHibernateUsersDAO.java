package data;

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

    @Step("[Hibernate] Remove user from database")
    @Override
    public void remove(UserEntity user) {
        remove(user);
    }

    @Step("[Hibernate] Get user from database by username '{username}'")
    @Override
    public UserEntity getByUsername(String username) {
        return em.createQuery("select u from UserEntity u where u.username=:username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
