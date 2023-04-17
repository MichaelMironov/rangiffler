package data.spring_jdbc;

import data.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsersRowMapper implements RowMapper<UserEntity> {
    @Override
    public UserEntity mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return UserEntity.builder()
                .id(UUID.fromString(rs.getString(1)))
                .username(rs.getString(2))
                .firstname(rs.getString(3))
                .lastname(rs.getString(4))
                .avatar(rs.getBytes(5))
                .build();
    }
}
