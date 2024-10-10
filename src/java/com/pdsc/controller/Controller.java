package com.pdsc.controller;

import com.pdsc.model.Denuncia;
import com.pdsc.util.Logging;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author willian santos
 */
public class Controller {
    
    
private static final String TAG = Controller.class.getSimpleName();
    protected EntityManagerFactory emf = null;

    public Controller() {
        this.emf = Persistence.createEntityManagerFactory("CampusResolvePU");
    }

    public void insert(Object objectList) {
        try {
            this.insert(new Object[]{objectList});
        } catch(Exception e){
            Logging.d(TAG, e.getMessage());
        }
    }

    public void insert(Object[] objectList) {
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            for (Object object : objectList) {
                em.persist(object);
            }
            em.flush();
            em.getTransaction().commit();
            em.close();
        } catch(Exception e){
            Logging.d(TAG, e.getMessage());
        }
    }

    public void update(Object objectList) {
        try {
            this.update(new Object[]{objectList});
        } catch(Exception e){
            Logging.d(TAG, e.getMessage());
        }
    }

    public void update(Object[] objectList) {
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            for (Object object : objectList) {
                em.merge(object);
            }
            em.getTransaction().commit();
            em.close();
        } catch(Exception e){
            Logging.d(TAG, e.getMessage());
        }
    }

    public List read(String query, Class c) {
        List list = null;
        try {
            EntityManager em = emf.createEntityManager();
            List returnedList = em.createQuery(query, c).getResultList();
            em.close();
            list = returnedList;
        } catch(Exception e){
            Logging.d(TAG, e.getMessage());
        }
        return list;
    }

    public void delete(Object[] objectList) {
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            for (Object object : objectList) {
                em.remove(object);
            }
            em.getTransaction().commit();
            em.close();
        } catch(Exception e){
            Logging.d(TAG, e.getMessage());
        }
    }
    
    public void delete(Object object) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Object managedEntity = em.find(object.getClass(), ((Denuncia) object).getId());
        if (managedEntity != null) {
            em.remove(managedEntity);
        }
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            Logging.d(TAG, e.getMessage());
        } finally {
            em.close();
        }
    }
}
