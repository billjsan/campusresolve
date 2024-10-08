package com.pdsc.controller;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.pdsc.model.Denuncia;
import com.pdsc.model.Usuario;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 *
 * @author PDSC
 */

@ManagedBean
@SessionScoped
public class DenunciaController extends Controller {
    
    private Denuncia denuncia;
    private List<Denuncia> listaDenunciasServidor;
    private List<Denuncia> listaDenunciasUsuario;
    private final SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    @PostConstruct
    public void init(){
        this.denuncia = new Denuncia();
    }
    
    public String inserirDenuncia() {
    FacesContext context = FacesContext.getCurrentInstance();

    // Validação do campo "Descrição"
    if (denuncia.getDescricaoDenuncia() == null || denuncia.getDescricaoDenuncia().trim().isEmpty()) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Descrição é obrigatória."));
        return null;  // Retorna nulo para não continuar o fluxo de navegação
    }
    if (denuncia.getDescricaoDenuncia().length() >= 250) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Descrição deve ter no máximo 250 caracteres."));
        return null;
    }

    // Validação do campo "Local do fato"
    if (denuncia.getLocalDenuncia() == null || denuncia.getLocalDenuncia().trim().isEmpty()) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Local do fato é obrigatório."));
        return null;
    }
    if (denuncia.getLocalDenuncia().length() >= 50) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Local do fato deve ter no máximo 50 caracteres."));
        return null;
    }

    // Se passar as validações, continua o processo de inserção
    Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(true))
        .getAttribute("loginController")).getUsuarioLogado();
    this.denuncia.setUsuario(usuarioLogado);
    this.denuncia.setEstadoDenuncia("N");
    insert(this.denuncia);
    this.denuncia = new Denuncia();
    return "indexUsuario";  // Redireciona para a página de sucesso
}

    
    public List<Denuncia> listarDenuncias(Usuario usuario){
        return read("select d from Denuncia d where d.usuario.id = " + usuario.getId(), Denuncia.class);
    }
    
    public List<Denuncia> listarDenunciasNovas(){
        //return read("select d from Denuncia d where d.localdenuncia = 'N'", Denuncia.class);
       return read("select d from Denuncia d ", Denuncia.class);

    }
    
    public String formatarData(Date data){
        return dataFormatada.format(data);
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public List<Denuncia> getListaDenunciasServidor() {
        return listaDenunciasServidor;
    }

    public void setListaDenunciasServidor(List<Denuncia> listaDenunciasServidor) {
        this.listaDenunciasServidor = listaDenunciasServidor;
    }

    public List<Denuncia> getListaDenunciasUsuario() {
        return listaDenunciasUsuario;
    }

    public void setListaDenunciasUsuario(List<Denuncia> listaDenunciasUsuario) {
        this.listaDenunciasUsuario = listaDenunciasUsuario;
    }
    
    
    
}
