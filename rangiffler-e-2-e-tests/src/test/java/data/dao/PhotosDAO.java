package data.dao;

import data.entity.PhotoEntity;

import javax.annotation.Nonnull;

public interface PhotosDAO extends DAO{

    void addPhoto(PhotoEntity photoEntity);

    void removePhotoByUsername(@Nonnull String username);

    PhotoEntity getPhotoByUsername(@Nonnull String username);
}
