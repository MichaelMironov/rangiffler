package data.dao;

import data.entity.UserEntity;
import data.jdbc.DataSourceContext;
import data.spring_jdbc.UsersRowMapper;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;

import static data.DataBase.AUTH;

public class PostgresSpringJdbcUsersDAO implements UsersDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresSpringJdbcUsersDAO.class);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceContext.INSTANCE.getDataSource(AUTH));

    @Step("[Spring-jdbc] Add user to database")
    @Override
    public int addUser(@Nonnull final UserEntity users) {
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
    public void updateUser(@Nonnull final UserEntity user) {
        jdbcTemplate.update("UPDATE users SET firstname = ?, lastname = ?  WHERE username = ?",
                user.getFirstname(),
                user.getLastname(),
                user.getUsername());
    }

    @Step("[Spring-jdbc] Remove user from database")
    @Override
    public void remove(@Nonnull final UserEntity user) {
        jdbcTemplate.update("DELETE from users WHERE username = ?", user.getUsername());
    }

    @Step("[Spring-jdbc] Remove user from database with name - {0}")
    public void removeByUsername(@Nonnull final String username) {
        getByUsername(username);
        jdbcTemplate.update("DELETE from users WHERE username = ?", username);
    }

    @Step("[Spring-jdbc] Get user from database by username '{username}'")
    @Override
    public UserEntity getByUsername(@Nonnull final String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                new UsersRowMapper(),
                username
        );
    }
}
