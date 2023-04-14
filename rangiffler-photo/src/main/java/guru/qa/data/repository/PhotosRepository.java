package guru.qa.data.repository;

import guru.qa.data.PhotoEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhotosRepository extends JpaRepository<PhotoEntity, UUID> {

    @Nullable
    List<PhotoEntity> findAllPhotosByUsername(@Nonnull String username);

}
