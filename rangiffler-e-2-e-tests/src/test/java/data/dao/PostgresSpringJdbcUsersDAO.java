package data.dao;

import data.UsersDAO;
import data.entity.UserEntity;
import data.jdbc.DataSourceContext;
import data.spring_jdbc.UsersRowMapper;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import static data.DataBase.USERDATA;

public class PostgresSpringJdbcUsersDAO implements UsersDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresSpringJdbcUsersDAO.class);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceContext.INSTANCE.getDataSource(USERDATA));

    @Step("[Spring-jdbc] Add user to database")
    @Override
    public int addUser(UserEntity users) {
        return jdbcTemplate.update("INSERT INTO users " +
                                   "(username, firstname, lastname, avatar)" +
                                   " VALUES (?, ?, ?, ?)",
                users.getUsername(),
                users.getFirstname(),
                users.getLastname(),
                new String(users.getAvatar())
        );
    }

    @Step("[Spring-jdbc] Update user in database")
    @Override
    public void updateUser(UserEntity user) {
        jdbcTemplate.update("UPDATE users SET firstname = ?, lastname = ?  WHERE username = ?",
                user.getFirstname(),
                user.getLastname(),
                user.getUsername());
    }

    @Step("[Spring-jdbc] Remove user from database")
    @Override
    public void remove(UserEntity user) {
        jdbcTemplate.update("DELETE from users WHERE id = ?", user.getId());
    }

    @Step("[Spring-jdbc] Get user from database by username '{username}'")
    @Override
    public UserEntity getByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                new UsersRowMapper(),
                username
        );
    }
}
