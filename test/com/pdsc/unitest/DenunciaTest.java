package com.pdsc.unitest;

import com.pdsc.model.Denuncia;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DenunciaTest {
    protected static EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction et;
    
    
    private final String setorDenunciaValida ="TI";
    private final String tipoDenunciaValida ="Assédio";
    private final String assundoDenunciaValida ="Assunto Teste";
    private final Date dataDenunciaValida =new Date();
    private final String localDenunciaValida ="Campus Recife";
    private final String descricaoDenunciaValida ="Denúncia sobre comportamento inadequado.";
    private final String estadoDenunciaValida ="N";
    private final Date dataCriacaoValida = new Date();
    
    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("CampusResolvePU");
    }

    @AfterClass
    public static void tearDownClass() {
        emf.close();
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        beginTransaction();
    }

    @After
    public void tearDown() {
        if (em.isOpen()) {
            rollbackTransaction();
            em.close();
        }
    }

    private void commitTransaction() {
        try {
            if (!et.getRollbackOnly()) {
                et.commit();
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar commit: " + e.getMessage());
            throw e;
        }
    }

    private void rollbackTransaction() {
        if (et.isActive()) {
            et.rollback();
        }
    }

    private void removerDenuncia(Denuncia d) {
        em.remove(em.contains(d) ? d : em.merge(d));
    }

    private void beginTransaction() {
        et = em.getTransaction();
        et.begin();
    }
    
    private Denuncia criarDenunciaValida() {
        Denuncia novaDenuncia = new Denuncia();
        novaDenuncia.setTipoDenuncia(tipoDenunciaValida);
        novaDenuncia.setAssundoDenuncia(assundoDenunciaValida);
        novaDenuncia.setDataDenuncia(dataDenunciaValida);
        novaDenuncia.setLocalDenuncia(localDenunciaValida);
        novaDenuncia.setDescricaoDenuncia(descricaoDenunciaValida);
        novaDenuncia.setEstadoDenuncia(estadoDenunciaValida);
        novaDenuncia.setDataCriacao(dataCriacaoValida);
        return novaDenuncia;
    }

    @Test
    public void testPersistenciaDenunciaValida() {
        Denuncia denunciaValida = criarDenunciaValida();
        em.persist(denunciaValida);
        commitTransaction();
        TypedQuery<Denuncia> query = em.createQuery("SELECT d FROM Denuncia d WHERE d.assundoDenuncia = :assunto", Denuncia.class);
        query.setParameter("assunto", assundoDenunciaValida);
        Denuncia result = query.getSingleResult();
        assertEquals(tipoDenunciaValida, result.getTipoDenuncia());
        assertEquals(assundoDenunciaValida, result.getAssundoDenuncia());
        assertNotNull(result.getDataDenuncia());
        assertEquals(localDenunciaValida, result.getLocalDenuncia());
        assertEquals(descricaoDenunciaValida, result.getDescricaoDenuncia());
        assertEquals(estadoDenunciaValida, result.getEstadoDenuncia());
        assertNotNull(result.getDataCriacao());
        beginTransaction();
        removerDenuncia(denunciaValida);
        commitTransaction();
        em.close();
    }

    @Test(expected =ConstraintViolationException.class)
    public void testSetorInvalidoCurto() {
        Denuncia denunciaInvalida = new Denuncia();
        denunciaInvalida.setTipoDenuncia("Assédio");
        denunciaInvalida.setAssundoDenuncia("Assunto Teste");
        denunciaInvalida.setDataDenuncia(new Date());
        denunciaInvalida.setLocalDenuncia("Campus Central");
        denunciaInvalida.setDescricaoDenuncia("Denúncia sobre comportamento inadequado.");
        denunciaInvalida.setEstadoDenuncia("N");
        denunciaInvalida.setDataCriacao(new Date());
        em.persist(denunciaInvalida);
        commitTransaction();
        beginTransaction();
        removerDenuncia(denunciaInvalida);
        commitTransaction();
        em.close();
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void testAssuntoInvalidoCurto() {
        Denuncia denunciaInvalida = new Denuncia();
        denunciaInvalida.setTipoDenuncia("Assédio");
        denunciaInvalida.setAssundoDenuncia("A");
        denunciaInvalida.setDataDenuncia(new Date());
        denunciaInvalida.setLocalDenuncia("Campus Central");
        denunciaInvalida.setDescricaoDenuncia("Denúncia sobre comportamento inadequado.");
        denunciaInvalida.setEstadoDenuncia("N");
        denunciaInvalida.setDataCriacao(new Date());
        em.persist(denunciaInvalida);
        commitTransaction();
        beginTransaction();
        removerDenuncia(denunciaInvalida);
        commitTransaction();
        em.close();
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void testEstadoDenunciaInvalido() {
        Denuncia denunciaInvalida = new Denuncia();
        denunciaInvalida.setTipoDenuncia("Assédio");
        denunciaInvalida.setAssundoDenuncia("Assunto Teste");
        denunciaInvalida.setDataDenuncia(new Date());
        denunciaInvalida.setLocalDenuncia("Campus Central");
        denunciaInvalida.setDescricaoDenuncia("Denúncia sobre comportamento inadequado.");
        denunciaInvalida.setEstadoDenuncia("X");
        denunciaInvalida.setDataCriacao(new Date());
        em.persist(denunciaInvalida);
        commitTransaction();
        beginTransaction();
        removerDenuncia(denunciaInvalida);
        commitTransaction();
        em.close();
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void testTipoDenunciaInvalidoCurto() {
        Denuncia denunciaInvalida = new Denuncia();
        denunciaInvalida.setTipoDenuncia("A");
        denunciaInvalida.setAssundoDenuncia("Assunto Teste");
        denunciaInvalida.setDataDenuncia(new Date());
        denunciaInvalida.setLocalDenuncia("Campus Central");
        denunciaInvalida.setDescricaoDenuncia("Denúncia sobre comportamento inadequado.");
        denunciaInvalida.setEstadoDenuncia("N");
        denunciaInvalida.setDataCriacao(new Date());
        em.persist(denunciaInvalida);
        commitTransaction();
        beginTransaction();
        removerDenuncia(denunciaInvalida);
        commitTransaction();
        em.close();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLocalInvalidoCurto() {
        Denuncia denunciaInvalida = new Denuncia();
        denunciaInvalida.setTipoDenuncia("Assédio");
        denunciaInvalida.setAssundoDenuncia("Assunto Teste");
        denunciaInvalida.setDataDenuncia(new Date());
        denunciaInvalida.setLocalDenuncia("L");
        denunciaInvalida.setDescricaoDenuncia("Denúncia sobre comportamento inadequado.");
        denunciaInvalida.setEstadoDenuncia("N");
        denunciaInvalida.setDataCriacao(new Date());
        em.persist(denunciaInvalida);
        commitTransaction();  
        beginTransaction();
        removerDenuncia(denunciaInvalida);
        commitTransaction();
        em.close();
    }
}
