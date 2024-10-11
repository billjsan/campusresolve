package com.pdsc.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.pdsc.model.Denuncia;
import com.pdsc.model.Usuario;
import com.pdsc.util.Logging;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.Date;
import javax.persistence.EntityManager;

/**
 *
 * @author Willian Santos
 */
@ManagedBean
@SessionScoped
public class DenunciaController extends Controller {

    private static final String TAG = DenunciaController.class.getSimpleName();
    private Denuncia denuncia;
    private List<Denuncia> listaDenunciasServidor;
    private List<Denuncia> listaDenunciasUsuario;
    private List<Denuncia> denunciasFiltradas;
    private final SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Denuncia denunciaSelecionada;
    private String tipoFiltro = null;

    @PostConstruct
    public void init() {
        this.denuncia = new Denuncia();
    }

    public Denuncia getDenunciaSelecionada() {
        return denunciaSelecionada;
    }

    public void setDenunciaSelecionada(Denuncia denuncia) {
        denunciaSelecionada = denuncia;
    }

    public void setTipoDeununcia(String tipo) {
        this.tipoFiltro = tipo;
    }
    
    public String getTipoFiltro() {
        return tipoFiltro;
    }
    
    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public List<Denuncia> listarDenunciasFiltradas() {
        if (tipoFiltro == null || tipoFiltro.isEmpty()) {
            return listarTodasDenuncias();
        } else {
            return filtrarDenuncias(tipoFiltro);
        }
    }
    
    public List<Denuncia> getDenunciasFiltradas() {
        if (tipoFiltro == null || tipoFiltro.isEmpty()) {
            return listarTodasDenuncias();
        } else {
            return filtrarDenuncias(tipoFiltro);
        }
    }

    public String inserirDenuncia() {
        Logging.d(TAG, "inserirDenuncia()");
        FacesContext context = FacesContext.getCurrentInstance();

        if (!descricaoEValida(denuncia.getDescricaoDenuncia(), context)) {
            return null;
        }

        if (!localDenunciaEValido(denuncia.getLocalDenuncia(), context)) {
            return null;
        }

        if (!dataDenunciaEValida(denuncia.getDataDenuncia(), context)) {
            return null;
        }

        if (!tipoDenunciaEValida(denuncia.getTipoDenuncia(), context)) {
            return null;
        }

        if (!assuntoDenunciaEValido(denuncia.getAssundoDenuncia(), context)) {
            return null;
        }

        Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true))
                .getAttribute("loginController")).getUsuarioLogado();
        this.denuncia.setUsuario(usuarioLogado);
        this.denuncia.setEstadoDenuncia("N");
        insert(this.denuncia);
        this.denuncia = new Denuncia();
        return "indexUsuario";
    }

    private boolean dataDenunciaEValida(Date data, FacesContext context) {
        Date dataAtual = new Date();
        if (data != null && data.after(dataAtual)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data da ocorrência não pode ser no futuro"));
            return false;
        }
        return true;
    }

    public List<Denuncia> filtrarDenuncias(String tipo) {
        List<Denuncia> result = new ArrayList();
        try {
            EntityManager em = emf.createEntityManager();
            result = (List<Denuncia>) (Denuncia) em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.tipoDenuncia = :tipoDenuncia")
                    .setParameter("tipoDenuncia", tipo);
            em.close();

        } catch (Exception e) {
            Logging.d(TAG, "erro em filtrarDenuncias() " + e.getMessage());
        }
        return result;
    }

    public String getNumeroDenunciaPorTipoString(String tipo) {
        String result = "0";
        try {
            EntityManager em = emf.createEntityManager();
            Long count = (Long) em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.tipoDenuncia = :tipoDenuncia")
                    .setParameter("tipoDenuncia", tipo)
                    .getSingleResult();
            em.close();
            result = count.toString();
        } catch (Exception e) {
            Logging.d(TAG, "erro em getNumeroDenunciaPorTipoString() " + e.getMessage());
        }
        return result;
    }

    private boolean localDenunciaEValido(String loc, FacesContext context) {
        if (loc == null || loc.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Local do fato é obrigatório."));
            return false;
        }
        if (loc.length() > 50 || loc.length() < 2) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Local do fato deve ter entre 2 e 50 caracteres."));
            return false;
        }
        return true;
    }

    private boolean descricaoEValida(String desc, FacesContext context) {
        if (desc == null || desc.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Descrição é obrigatória."));
            return false;
        }
        if (desc.length() > 5000 || desc.length() < 50) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Descrição deve ter entre 50 e 5000 caracteres."));
            return false;
        }
        return true;
    }

    private boolean tipoDenunciaEValida(String desc, FacesContext context) {
        if (desc == null || desc.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Tipo de denúncia é obrigatória."));
            return false;
        }
        return true;
    }

    private boolean assuntoDenunciaEValido(String desc, FacesContext context) {
        if (desc == null || desc.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Assunto de denúncia é obrigatório."));
            return false;
        }
        return true;
    }

    public List<Denuncia> listarDenunciasParaUsuario(Usuario usuario) {
        return read("select d from Denuncia d where d.usuario.id = " + usuario.getId(), Denuncia.class);
    }

    public List<Denuncia> listarTodasDenuncias() {
        return read("select d from Denuncia d ", Denuncia.class);
    }

    public String formatarData(Date data) {
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

    public void atualizarDenuncia() {
        if (denunciaSelecionada != null) {
            update(denunciaSelecionada);
        }
    }

    public void excluirDenuncia() {
        if (denunciaSelecionada != null) {
            delete(denunciaSelecionada);
        }
    }
}
