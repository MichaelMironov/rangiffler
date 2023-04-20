package data.dao;

import data.entity.CountryEntity;

import java.util.UUID;

public interface CountryDAO extends DAO {

    CountryEntity getCountryById(UUID id);

    CountryEntity getCountryByName(String name);

}
