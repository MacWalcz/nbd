package org.nbd;

import jakarta.persistence.*;

public abstract class JPARepoManager<T> implements RepoManager<T> {

    private EntityManagerFactory emf;
    protected EntityManager em ;
    private String link;


    public JPARepoManager(String link) {
        this.link = link;
        emf = Persistence.createEntityManagerFactory(link);
        this.em = emf.createEntityManager();

    }

    @Override
    public void create(T entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(entity);
        transaction.commit();
    }


    @Override
    public void update(long id, T entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(entity);
        try {
            transaction.commit();
        } catch (OptimisticLockException e) {
            transaction.rollback();
        }
        transaction.commit();
    }

    @Override
    public void delete(long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(em.find(Entity.class, id));
        transaction.commit();
    }
}
