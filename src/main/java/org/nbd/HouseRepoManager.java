package org.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;

public class HouseRepoManager extends JPARepoManager<House> {
    public HouseRepoManager(String link) {
        super(link);
    }

    @Override
    public House read(long id) {
        return em.find(House.class, id);
    }

    @Override
    public void delete(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(em.find(House.class, id));
        transaction.commit();
    }

    @Override
    public void update(long id, House entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        House house = em.find(House.class, id, LockModeType.PESSIMISTIC_WRITE);

        house.setHouseNumber(entity.getHouseNumber());
        house.setArea(entity.getArea());
        house.setPrice(entity.getPrice());

        em.merge(house);
        transaction.commit();
    }

}
