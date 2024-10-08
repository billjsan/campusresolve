package com.pdsc.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author PDSC
 */
public class Controller {

    protected EntityManagerFactory emf = null;

    public Controller() {
        this.emf = Persistence.createEntityManagerFactory("CampusResolvePU");
    }

    public void insert(Object objectList) {
        this.insert(new Object[]{objectList});
    }

    public void insert(Object[] objectList) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        for (Object object : objectList) {
            em.persist(object);
        }

        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void update(Object objectList) {
        this.update(new Object[]{objectList});
    }

    public void update(Object[] objectList) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        for (Object object : objectList) {
            em.merge(object);
        }

        em.getTransaction().commit();
        em.close();
    }

    public List read(String query, Class c) {

        EntityManager em = emf.createEntityManager();

        List returnedList = em.createQuery(query, c).getResultList();

        em.close();

        return returnedList;
    }

    public void delete(Object[] objectList) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        for (Object object : objectList) {
            em.remove(object);
        }
        
        em.getTransaction().commit();
        em.close();
    }
}
