package data.dao;

import data.entity.PhotoEntity;
import data.jdbc.DataSourceContext;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;

import static data.DataBase.PHOTO;

public class PostgresSpringJdbcPhotosDAO implements PhotosDAO {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceContext.INSTANCE.getDataSource(PHOTO));

    @Step("[Spring-Jdbc] Delete user photo with name - {0}")
    public void removePhotoByUsername(@Nonnull String username) {
        jdbcTemplate.update("DELETE from photos WHERE username = ?;", username);
    }

    @Override
    @Step("[Spring-Jdbc] Add photo in database")
    public void addPhoto(@Nonnull final PhotoEntity photoEntity) {
        jdbcTemplate.update("INSERT INTO photos (country_id, photo, description, username)" +
                            "VALUES (?, ?, ?, ?)",
                photoEntity.getCountryId(),
                photoEntity.getPhoto(),
                photoEntity.getDescription(),
                photoEntity.getUsername());
    }

    @Override
    public PhotoEntity getPhotoByUsername(@NotNull final String username) {
        return null;
    }
}
