package data.dao;

import data.DataBase;
import data.entity.CountryEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;

import java.util.UUID;

public class PostgresHibernateCountriesDAO extends JpaService implements CountryDAO {

    public PostgresHibernateCountriesDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.PHOTO).createEntityManager());
    }

    @Override
    public CountryEntity getCountryById(UUID id) {
        return em.find(CountryEntity.class, id);
    }

    @Override
    public CountryEntity getCountryByName(String name) {
        return em.createQuery("select c from CountryEntity c where c.name=:name", CountryEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
