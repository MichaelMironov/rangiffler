package data.dao;

import data.entity.CountryEntity;
import data.entity.PhotoEntity;

import javax.annotation.Nonnull;

public interface PhotosDAO extends DAO{

    void addPhotoByUsername(String username, CountryEntity country, String description , byte[] photo);

    PhotoEntity removePhotoByUsername(@Nonnull String username);

    PhotoEntity getPhotoByUsername(@Nonnull String username);
}
