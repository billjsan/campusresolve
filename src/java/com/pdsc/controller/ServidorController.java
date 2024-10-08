package com.pdsc.controller;

import com.pdsc.model.Servidor;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Controlador para gerenciamento de Servidores.
 */
@ManagedBean
@SessionScoped
public class ServidorController extends Controller {

    private Servidor servidorCadastro;
    private Servidor selection;

    @PostConstruct
    public void init() {
        this.servidorCadastro = new Servidor();
        this.selection = new Servidor();
    }

    /**
     * Insere um novo servidor no banco de dados após validação.
     * 
     * @param confirma Senha para confirmação.
     */
    public void inserir(String confirma) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (!servidorCadastro.getSenha().equals(confirma)) {
            context.addMessage("formRegistroServidor:senha",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "A senha e a confirmação não são iguais!"));
            return;
        }

        try {
            insert(servidorCadastro);
            servidorCadastro = new Servidor(); // Limpa o formulário
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Servidor cadastrado com sucesso!"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível cadastrar o servidor."));
            e.printStackTrace(); // Log de erro para diagnóstico
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
