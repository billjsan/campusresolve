
package com.pdsc.unitest;
import com.pdsc.model.Usuario;
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


public class UsuarioTest  {
    
    protected static EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction et;
    private Usuario usuarioValido;
    
    // Info usuário valido
    private final String matriculaUsuarioValido = "55555";
    private final String nomeUsuarioValido = "João Henrique";
    private final String senhaUsuarioValido = "Password1@";
    
    private Usuario usuarioInValido;

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
        criarUsuarioValido();
    }

    @After
    public void tearDown() {
        removerUsuarioCriado();
        commitTransaction();
        em.close();
    }

    private void beginTransaction() {
        et = em.getTransaction();
        et.begin();
    }

    private void commitTransaction() {
       try {
        if (!et.getRollbackOnly()) {
            et.commit();
        }
       } catch(Exception e) {
       }
    }

    private void criarUsuarioValido() {
        usuarioValido = new Usuario();
        usuarioValido.setMatricula(matriculaUsuarioValido);
        usuarioValido.setNome(nomeUsuarioValido);
        usuarioValido.setSenha(senhaUsuarioValido);
        em.persist(usuarioValido);
    }


    private void removerUsuarioCriado() {
        em.remove(em.contains(usuarioValido) ? usuarioValido : em.merge(usuarioValido));
    }


    @Test
    public void testNome() {
        TypedQuery<String> query = em.createQuery("SELECT u.nome FROM Usuario u WHERE u.matricula = :matricula", String.class);
        query.setParameter("matricula", matriculaUsuarioValido);
        String result = query.getSingleResult();
        assertEquals(nomeUsuarioValido, result);
    }

    @Test
    public void testSenha() {
        TypedQuery<String> query = em.createQuery("SELECT u.senha FROM Usuario u WHERE u.matricula = :matricula", String.class);
        query.setParameter("matricula", matriculaUsuarioValido);
        String result = query.getSingleResult();
        assertTrue(result.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$"));
    }

    @Test
    public void testMatriculaSize() {
        TypedQuery<String> query = em.createQuery("SELECT u.matricula FROM Usuario u WHERE u.matricula = :matricula", String.class);
        query.setParameter("matricula", matriculaUsuarioValido);
        String result = query.getSingleResult();
        assertTrue(result.length() >= 4 && result.length() <= 10);
    }

    @Test
    public void testNomeSize() {
        TypedQuery<String> query = em.createQuery("SELECT u.nome FROM Usuario u WHERE u.matricula = :matricula", String.class);
        query.setParameter("matricula", matriculaUsuarioValido);
        String result = query.getSingleResult();
        assertTrue(result.length() >= 2 && result.length() <= 50);
    }

    @Test
    public void testSenhaSize() {
        TypedQuery<String> query = em.createQuery("SELECT u.senha FROM Usuario u WHERE u.matricula = :matricula", String.class);
        query.setParameter("matricula", matriculaUsuarioValido);
        String result = query.getSingleResult();
        assertTrue(result.length() >= 6 && result.length() <= 20);
    }

     @Test(expected = ConstraintViolationException.class)
    public void testSenhaInvalidaCurta() {
        usuarioInValido = new Usuario();
        usuarioInValido.setMatricula("55556");
        usuarioInValido.setNome("Carlos");
        usuarioInValido.setSenha("123");
        em.persist(usuarioInValido);
        commitTransaction();
    }
    
     @Test(expected = ConstraintViolationException.class)
    public void testSenhaInvalidaSemCaracterEspecial() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("55557");
        usuario.setNome("Pedro");
        usuario.setSenha("Password1");  // Falta caractere especial
        em.persist(usuario);
        commitTransaction();
    }
    
    
    @Test(expected = ConstraintViolationException.class)
    public void testNomeInvalidoCurto() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("55558");
        usuario.setNome("A");  // Nome muito curto, inválido
        usuario.setSenha("Password1@");
        em.persist(usuario);
        commitTransaction();
    }
    
      @Test(expected = ConstraintViolationException.class)
    public void testNomeInvalidoLongo() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("55559");
        usuario.setNome("NomeMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoLongo");  // Nome muito longo
        usuario.setSenha("Password1@");
        em.persist(usuario);
        commitTransaction();
    }
    
}
