package com.pdsc.controller;

import com.pdsc.model.Servidor;
import com.pdsc.model.Usuario;
import com.pdsc.util.Logging;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Willian Santos
 */

@ManagedBean
@SessionScoped
public class LoginController extends Controller implements Serializable{
    
    private static final String TAG = LoginController.class.getSimpleName();
    private Usuario usuarioLogado;
    private Servidor servidorLogado;

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public Servidor getServidorLogado() {
        return servidorLogado;
    }

    public void setServidorLogado(Servidor servidorLogado) {
        this.servidorLogado = servidorLogado;
    }
    
    public String loginServidor(String matricula, String senha) {
        try {
            this.servidorLogado  = (Servidor)read("select p from Servidor p" + " where p.matricula = '" + matricula + "' and p.senha = '" + senha+"'", 
                            Servidor.class).get(0);                        
            return "indexServidor";
        } catch (Exception e) {            
            Logging.d(TAG, e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao Logar","Matricula e/ou senha estão incorretos"));
            return null;
        }
    }
    
    public String loginUsuario(String matricula, String senha) {
        Logging.d(TAG, "loginUsuario");
        try {
            this.usuarioLogado  = (Usuario)read("select p from Usuario p where p.matricula = '" + matricula + "' and p.senha = '" + senha + "'", 
                            Usuario.class).get(0);                        
            return "indexUsuario";
        } catch (Exception e) {   
            Logging.d(TAG, e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao Logar","Matricula e/ou senha estão incorretos"));
            return null;
        }
    }
    
    public String logout(){
        this.usuarioLogado = null;
        this.servidorLogado = null;
        return "login";
    }
    
}
