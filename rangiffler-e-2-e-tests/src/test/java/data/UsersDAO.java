package data;

import data.entity.UserEntity;

public interface UsersDAO extends DAO {
    int addUser(UserEntity users);

    void updateUser(UserEntity user);

    void remove(UserEntity user);

    UserEntity getByUsername(String username);
}
