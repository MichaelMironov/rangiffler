package data.dao;

import data.DataBase;
import data.entity.PhotoEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import org.jetbrains.annotations.NotNull;

public class PostgresHibernatePhotosDAO extends JpaService implements PhotosDAO {


    public PostgresHibernatePhotosDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.PHOTO).createEntityManager());
    }

    @Override
    public void addPhoto(PhotoEntity photoEntity) {
        merge(photoEntity);
    }

    @Override
    public void removePhotoByUsername(@NotNull final String username) {
        em.createQuery("delete p from PhotoEntity p where p.username=:username", PhotoEntity.class)
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
