
package com.pdsc.unitest;

import com.pdsc.model.Servidor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServidorTest {
     protected static EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction et;
    private Servidor servidor;

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

    private void commitTransaction() {
       try {
        if (!et.getRollbackOnly()) {
            et.commit();
        }
       } catch(Exception e) {
       }
    }
    
     private void removerServidor(Servidor s) {
          em.remove(em.contains(s) ? s : em.merge(s));
    }
     
    private void beginTransaction() {
        et = em.getTransaction();
        et.begin();
    }

    private void criarServidorValido() {
        servidor = new Servidor();
        servidor.setMatricula("54321");
        servidor.setNome("Carlos Silva");
        servidor.setCargo("Administrador");
        servidor.setSetor("TI");
        servidor.setSenha("Password1@");
        em.persist(servidor);
    }

    @Test
    public void testPersistenciaServidorValido() {
        Servidor servidorValido = new Servidor();
        servidorValido.setMatricula("54321");
        servidorValido.setNome("Carlos Silva");
        servidorValido.setCargo("Administrador");
        servidorValido.setSetor("TI");
        servidorValido.setSenha("Password1@");
        em.persist(servidorValido);
        commitTransaction();
        TypedQuery<Servidor> query = em.createQuery("SELECT s FROM Servidor s WHERE s.matricula = :matricula", Servidor.class);
        query.setParameter("matricula", "54321");
        Servidor result = query.getSingleResult();
        assertEquals("Carlos Silva", result.getNome());
        beginTransaction(); 
        removerServidor(servidorValido);
        commitTransaction();
        em.close();
    }

    @Test(expected = ConstraintViolationException.class)
        public void testSenhaInvalidaCurta() {
            Servidor servidorSenhaCurta = new Servidor();
            servidorSenhaCurta.setMatricula("54322");
            servidorSenhaCurta.setNome("Ana Lima");
            servidorSenhaCurta.setCargo("Secretário");
            servidorSenhaCurta.setSetor("Administração");
            servidorSenhaCurta.setSenha("123");  // Senha muito curta
            em.persist(servidorSenhaCurta);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testSenhaInvalidaSemCaracterEspecial() {
            Servidor servidorSemCaracter = new Servidor();
            servidorSemCaracter.setMatricula("54323");
            servidorSemCaracter.setNome("Pedro Souza");
            servidorSemCaracter.setCargo("Gerente");
            servidorSemCaracter.setSetor("Financeiro");
            servidorSemCaracter.setSenha("Password1");  // Falta caractere especial
            em.persist(servidorSemCaracter);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testNomeInvalidoCurto() {
           Servidor  servidorNomeInvalido = new Servidor();
            servidorNomeInvalido.setMatricula("54324");
            servidorNomeInvalido.setNome("A");  // Nome muito curto
            servidorNomeInvalido.setCargo("Coordenador");
            servidorNomeInvalido.setSetor("Recursos Humanos");
            servidorNomeInvalido.setSenha("Password1@");
            em.persist(servidorNomeInvalido);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testNomeInvalidoLongo() {
            Servidor  servidorNomeInvalido = new Servidor();
            servidorNomeInvalido.setMatricula("54325");
            servidorNomeInvalido.setNome("NomeMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoLongo");  // Nome muito longo
            servidorNomeInvalido.setCargo("Gerente");
            servidorNomeInvalido.setSetor("Recursos Humanos");
            servidorNomeInvalido.setSenha("Password1@");
            em.persist(servidorNomeInvalido);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testCargoInvalidoCurto() {
            Servidor servidorCargoInvalido = new Servidor();
            servidorCargoInvalido.setMatricula("54326");
            servidorCargoInvalido.setNome("Ana Clara");
            servidorCargoInvalido.setCargo("A");  // Cargo muito curto
            servidorCargoInvalido.setSetor("Administração");
            servidorCargoInvalido.setSenha("Password1@");
            em.persist(servidorCargoInvalido);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testCargoInvalidoLongo() {
            Servidor servidorCargoInvalidoLongo = new Servidor();
            servidorCargoInvalidoLongo.setMatricula("54327");
            servidorCargoInvalidoLongo.setNome("Jorge Ferreira");
            servidorCargoInvalidoLongo.setCargo("CargoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoLongo");  // Cargo muito longo
            servidorCargoInvalidoLongo.setSetor("TI");
            servidorCargoInvalidoLongo.setSenha("Password1@");
            em.persist(servidorCargoInvalidoLongo);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testSetorInvalidoCurto() {
            Servidor servidorInvalido = new Servidor();
            servidorInvalido.setMatricula("54328");
            servidorInvalido.setNome("Carlos Lima");
            servidorInvalido.setCargo("Supervisor");
            servidorInvalido.setSetor("A");  // Setor muito curto
            servidorInvalido.setSenha("Password1@");
            em.persist(servidorInvalido);
            commitTransaction();
        }

    @Test(expected = ConstraintViolationException.class)
        public void testSetorInvalidoLongo() {
            Servidor servidorSetorInvalidoLongo = new Servidor();
            servidorSetorInvalidoLongo.setMatricula("54329");
            servidorSetorInvalidoLongo.setNome("Fernanda Martins");
            servidorSetorInvalidoLongo.setCargo("Diretor");
            servidorSetorInvalidoLongo.setSetor("SetorMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoLongo");  // Setor muito longo
            servidorSetorInvalidoLongo.setSenha("Password1@");
            em.persist(servidorSetorInvalidoLongo);
            commitTransaction();
        }
    
}