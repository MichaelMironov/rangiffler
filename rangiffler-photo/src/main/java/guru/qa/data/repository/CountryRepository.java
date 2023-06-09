package guru.qa.data.repository;

import guru.qa.data.CountryEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
    @Nullable
    CountryEntity findByCode(@Nonnull String username);
}
