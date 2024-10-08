package com.pdsc.controller;

import com.pdsc.model.Usuario;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Controlador responsável por gerenciar as ações relacionadas à entidade Usuario.
 * PDSC
 */
@ManagedBean
@SessionScoped
public class UsuarioController extends Controller {

    private Usuario usuarioCadastro;
    private Usuario selection;
    private Validator validator;
    
    @PostConstruct
    public void init() {
        this.usuarioCadastro = new Usuario();
        this.selection = new Usuario();
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            this.validator = factory.getValidator();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Validator Initialized", "Validator initialized successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Initializing Validator", e.getMessage()));
        }
    }

    public void inserir(String confirma) {
        if (!usuarioCadastro.getSenha().equals(confirma)) {
            FacesContext.getCurrentInstance().addMessage("formRegistroUsuario:senha",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "A senha e a confirmação não são iguais!"));
            return;
        }

        // Validar a entidade
        //Set<ConstraintViolation<Usuario>> violations = validator.validate(usuarioCadastro);
        //if (!violations.isEmpty()) {
        //    for (ConstraintViolation<Usuario> violation : violations) {
        //        FacesContext.getCurrentInstance().addMessage(null,
        //                new FacesMessage(FacesMessage.SEVERITY_ERROR, violation.getMessage(), null));
        //    }
        //    return;
        //}

        insert(usuarioCadastro);
        usuarioCadastro = new Usuario();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Usuário cadastrado com sucesso!"));
    }

    public List<Usuario> readAll() {
        return read("select p from Usuario p", Usuario.class);
    }

    // Getters e Setters
    public Usuario getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Usuario usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public Usuario getSelection() {
        return selection;
    }

    public void setSelection(Usuario selection) {
        this.selection = selection;
    }
}
