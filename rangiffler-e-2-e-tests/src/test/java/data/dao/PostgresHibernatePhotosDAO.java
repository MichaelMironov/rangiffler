package data.dao;

import data.DataBase;
import data.entity.CountryEntity;
import data.entity.PhotoEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class PostgresHibernatePhotosDAO extends JpaService implements PhotosDAO {


    public PostgresHibernatePhotosDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.PHOTO).createEntityManager());
    }


    @Override
    public void addPhotoByUsername(final String username, CountryEntity country, final String description, final byte[] photo) {

        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setPhoto(photo);
        photoEntity.setUsername(username);
        photoEntity.setCountryId(country.getId());
        photoEntity.setDescription(description);

        try {
            em.createNativeQuery("insert into photos (country_id, photo, description, username) " +
                                 "values (?,?,?,?);", PhotoEntity.class)
                    .setParameter(1, country.getId())
                    .setParameter(2, new String(photo, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8))
                    .setParameter(3, description)
                    .setParameter(4, username)
                    .getSingleResult();
        }catch (Exception e) {

        }


    }

    @Override
    public PhotoEntity removePhotoByUsername(@NotNull final String username) {
        return em.createQuery("delete p from PhotoEntity p where p.username=:username", PhotoEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public PhotoEntity getPhotoByUsername(@NotNull final String username) {
        return em.createQuery("select photo p from PhotoEntity p where p.username=:username", PhotoEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
