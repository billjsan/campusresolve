package com.pdsc.controller;

import com.pdsc.model.Denuncia;
import com.pdsc.util.Logging;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import static org.eclipse.persistence.config.ResultType.Map;

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
            Logging.d(TAG, "insert(): objeto antes de inserir: " + objectList.toString());
            this.insert(new Object[]{objectList});
        } catch(Exception e){
            Logging.d(TAG, "insert(): " + e.getMessage());
        }
    }

    public void insert(Object[] objectList) throws Exception {
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
           Logging.d(TAG, "ERROR on=> insert(): " + e.getMessage()); 
            e.printStackTrace();
            throw e;  
        }
    }

    public void update(Object objectList) throws Exception {
        try {
            this.update(new Object[]{objectList});
        } catch(Exception e){
            Logging.d(TAG, "ERROR on=> update(): " + e.getMessage());
            throw new Exception(); 
        }
    }

    public void update(Object[] objectList) throws Exception {
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            for (Object object : objectList) {
                em.merge(object);
            }
            em.getTransaction().commit();
            em.close();
        } catch(Exception e){
            Logging.d(TAG, "ERROR on=> update(): " + e.getMessage());
            throw new Exception();   
        }
    }

    public <T> List<T> read(String query, Class c) {
        List list = null;
        try {
            EntityManager em = emf.createEntityManager();
            List returnedList = em.createQuery(query, c).getResultList();
            em.close();
            list = returnedList;
        } catch(Exception e){
            Logging.d(TAG, "read(): " + e.getMessage());
        }
        return list;
    }
    
    public <T> List<T> read(String query, Class<T> c, Map<String, Integer> parameters) {
        List<T> list = null;
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            TypedQuery<T> typedQuery = em.createQuery(query, c);
            if (parameters != null) {
                parameters.forEach(typedQuery::setParameter);
            }
            list = typedQuery.getResultList();
        } catch (Exception e) {
            Logging.d(TAG, "read(): " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
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
            Logging.d(TAG,"delete(): " +  e.getMessage());
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
            Logging.d(TAG, "delete(): " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
