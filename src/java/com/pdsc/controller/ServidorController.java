package com.pdsc.controller;

import com.pdsc.model.Servidor;
import com.pdsc.util.Logging;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;


@ManagedBean
@SessionScoped
public class ServidorController extends Controller {

    private static final String TAG = ServidorController.class.getSimpleName();
    private Servidor servidorCadastro;
    private Servidor selection;

    @PostConstruct
    public void init() {
        this.servidorCadastro = new Servidor();
        this.selection = new Servidor();
    }

    public void inserir(String confirma) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!servidorCadastro.getSenha().equals(confirma)) {
            context.addMessage("formRegistroServidor:senha",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "A senha e a confirmação não são iguais!"));
            return;
        }

        if (ePrimeiroServidor()) {
            servidorCadastro.setFuncao(Servidor.ADM);
        }
        
        try {
            insert(servidorCadastro);
            servidorCadastro = new Servidor();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Servidor cadastrado com sucesso!"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível cadastrar o servidor."));
            Logging.d(TAG, "erro ao inserir servidor " + e.getMessage());
        }
    }
    
    private boolean ePrimeiroServidor() {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = (Long) em.createQuery("SELECT COUNT(s) FROM Servidor s").getSingleResult();
            return count == 0;
        } catch (Exception e) {
            Logging.d(TAG, "erro em verificar o banco " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos os servidores do banco de dados.
     * 
     * @return Lista de servidores.
     */
    public List<Servidor> readAll() {
        try {
            return read("select p from Servidor p", Servidor.class);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível recuperar a lista de servidores."));
            e.printStackTrace(); // Log de erro para diagnóstico
            return null;
        }
    }

    // Getters e Setters
    public Servidor getServidorCadastro() {
        return servidorCadastro;
    }

    public void setServidorCadastro(Servidor servidorCadastro) {
        this.servidorCadastro = servidorCadastro;
    }

    public Servidor getSelection() {
        return selection;
    }

    public void setSelection(Servidor selection) {
        this.selection = selection;
    }
}
