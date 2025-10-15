package org.nbd;

import jakarta.persistence.*;

public class RentRepoManager extends JPARepoManager<Rent> {
    public RentRepoManager(String link) {
        super(link);
    }

    @Override
    public Rent read(long id) {
        return em.find(Rent.class, id);
    }

    @Override
    public void create(Rent entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        
        House house = entity.getHouse();

        boolean overlapping = em.createQuery("""
        SELECT COUNT(r) FROM Rent r
        WHERE r.house = :house
          AND r.startDate < :to
          AND r.endDate > :from
        """, Long.class)
                .setParameter("house", house)
                .setParameter("from", entity.getStartDate())
                .setParameter("to", entity.getEndDate())
                .getSingleResult() > 0;

        if (overlapping) {
            transaction.rollback();
        } else {
            em.persist(entity);
            transaction.commit();
        }
    }

    @Override
    public void delete(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(em.find(Rent.class, id));
        transaction.commit();
    }

    @Override
    public void update(long id, Rent entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Rent rent = em.find(Rent.class, id, LockModeType.PESSIMISTIC_WRITE);

        House house = entity.getHouse();

        boolean overlapping = em.createQuery("""
        SELECT COUNT(r) FROM Rent r
        WHERE r.house = :house
          AND r.startDate < :to
          AND r.endDate > :from
        """, Long.class)
                .setParameter("house", house)
                .setParameter("from", entity.getStartDate())
                .setParameter("to", entity.getEndDate())
                .getSingleResult() > 0;

        if (overlapping) {
            transaction.rollback();
        } else {
            rent.setStartDate(entity.getStartDate());
            rent.setEndDate(entity.getEndDate());
            rent.setCost(entity.getCost());

            em.merge(rent);
            transaction.commit();
        }
    }
}