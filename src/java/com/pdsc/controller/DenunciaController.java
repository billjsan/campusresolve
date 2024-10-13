package com.pdsc.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.pdsc.model.Denuncia;
import com.pdsc.model.Servidor;
import com.pdsc.model.Usuario;
import com.pdsc.util.Logging;
import java.io.UnsupportedEncodingException;
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
    private final SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Denuncia denunciaSelecionada;
    private String filtroDenuncia;

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

    public void setFiltroDenuncia(String filtro) {
        filtroDenuncia = filtro;
    }
    
    public String getFiltroDenuncia() {
        return filtroDenuncia;
    }

    public List<Denuncia> getDenunciaFiltradas() {
        if (filtroDenuncia == null || filtroDenuncia.isEmpty()) {
            return getTodasDenuncias();
        } else {
            return getDenunciasPorTipo(filtroDenuncia);
        }
    }

    public String inserirDenuncia() {
        Logging.d(TAG, "inserirDenuncia()");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!descricaoEValida(denuncia.getDescricaoDenuncia(), context) ||
                !localDenunciaEValido(denuncia.getLocalDenuncia(), context) ||
                !dataDenunciaEValida(denuncia.getDataDenuncia(), context) ||
                !tipoDenunciaEValida(denuncia.getTipoDenuncia(), context) ||
                !assuntoDenunciaEValido(denuncia.getAssundoDenuncia(), context)) {
                return null;
            }

            Servidor servidorResponsavel = getServidorResponsavel();
            if (servidorResponsavel == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Nenhum servidor responsável foi encontrado"));
                return null;
            }

            Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(true))
                    .getAttribute("loginController")).getUsuarioLogado();

            if (usuarioLogado == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Usuário não está logado"));
                return null;
            }

            denuncia.setUsuario(usuarioLogado);
            denuncia.setServidor(servidorResponsavel);
            denuncia.setEstadoDenuncia("N");
            denuncia.setDataCriacao(new Date());

            if(!eDenunciaValida(denuncia)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Denúncia não é valida"));
                return null;
            } 
            
            usuarioLogado.getDenuncias().add(denuncia);
            servidorResponsavel.getDenuncias().add(denuncia);
            
            insert(denuncia);
            update(usuarioLogado);
            update(servidorResponsavel);

            this.denuncia = new Denuncia();

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso!", "Denúncia registrada com sucesso."));
            return "indexUsuario";

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro!", "Ocorreu um problema ao registrar a denúncia."));
            Logging.d(TAG, "erro em inserirDenuncia() " + e.getMessage());
            return null;
        }
    }
    
    private boolean eDenunciaValida(Denuncia d) {
        boolean isValid = true;
        if (d.getAssundoDenuncia() == null) {
            Logging.d(TAG, "Campo 'assuntoDenuncia' está nulo");
            isValid = false;
        }
        if (d.getDataCriacao() == null) {
            Logging.d(TAG, "Campo 'dataCriacao' está nulo");
            isValid = false;
        }
        if (d.getDataDenuncia() == null) {
            Logging.d(TAG, "Campo 'dataDenuncia' está nulo");
            isValid = false;
        }
        if (d.getDescricaoDenuncia() == null) {
            Logging.d(TAG, "Campo 'descricaoDenuncia' está nulo");
            isValid = false;
        }
        if (d.getUsuario() == null) {
            Logging.d(TAG, "Campo 'usuario' está nulo");
            isValid = false;
        }
        if (d.getServidor() == null) {
            Logging.d(TAG, "Campo 'servidor' está nulo");
            isValid = false;
        }
        if (d.getTipoDenuncia() == null) {
            Logging.d(TAG, "Campo 'tipoDenuncia' está nulo");
            isValid = false;
        }
        if (d.getLocalDenuncia() == null) {
            Logging.d(TAG, "Campo 'localDenuncia' está nulo");
            isValid = false;
        }
        if (d.getEstadoDenuncia() == null) {
            Logging.d(TAG, "Campo 'estadoDenuncia' está nulo");
            isValid = false;
        }
        if (isValid) {
            Logging.d(TAG, "Todos os campos foram preenchidos corretamente.");
        }
        return isValid;
    }

    public List<Denuncia> getDenunciasPorTipo(String tipo) {
        List<Denuncia> result = new ArrayList();
        try {
            EntityManager em = emf.createEntityManager();
            result = em.createQuery("SELECT d FROM Denuncia d WHERE d.tipoDenuncia = :tipoDenuncia", Denuncia.class)
                   .setParameter("tipoDenuncia", tipo)
                   .getResultList();
                    em.close();
        } catch (Exception e) {
            Logging.d(TAG, "erro em filtrarDenuncias() " + e.getMessage());
        }
        return result;
    }
    
    private Servidor getServidorResponsavel() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Servidor> servidores = (List<Servidor>) em.createQuery("SELECT s FROM Servidor s").getResultList();
            if(servidores == null || servidores.isEmpty()) {
                Logging.d(TAG, "nenhum servidor encontrado");
                return null;
            }
            List<Servidor> servidoresTriagem = new ArrayList<>();
            servidores.stream().filter((s) -> (s.getFuncao().equals(Servidor.TRIAGEM))).forEachOrdered((s) -> {
                servidoresTriagem.add(s);
            });
            
            if(servidoresTriagem.isEmpty()) {
                Logging.d(TAG, "nenhum servidor responsavel encontrado");
                return null;
            }
            Servidor servidorResponsavel = servidoresTriagem.get(0);
            for(Servidor r: servidoresTriagem) {
                if(r.getDenuncias().size() < servidorResponsavel.getDenuncias().size()) {
                    servidorResponsavel = r;
                    return servidorResponsavel;
                }            
            }
            return servidorResponsavel;
        } catch (Exception e) {
            Logging.d(TAG, "erro ao pegar servidor responsavel " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    public String getQuantidadeOcorrenciaPorTipoString(String tipo) {
        String result = "0";
        try {
            if ("todas".equals(tipo)) {
                return String.valueOf(getTodasDenuncias().size());
            }
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

    private boolean dataDenunciaEValida(Date data, FacesContext context) {
        Date dataAtual = new Date();
        if (data != null && data.after(dataAtual)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data da ocorrência não pode ser no futuro"));
            return false;
        }
        return true;
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
            Logging.d(TAG, "descrição é vazia ou nula");
            return false;
        }

        String descricaoNormalizada = desc.trim().replaceAll("\\s+", " ");
        Logging.d(TAG, "descricaoEValida(): tamanho normalizado: " + descricaoNormalizada.length());

        try {
            int byteLength = descricaoNormalizada.getBytes("UTF-8").length;
            Logging.d(TAG, "Tamanho em bytes: " + byteLength);
            if (byteLength > 5500) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Descrição excede o limite de 5000 bytes."));
                Logging.d(TAG, "Descrição excede 5500 bytes");
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            Logging.d(TAG, "Erro ao calcular o tamanho em bytes: " + e.getMessage());
            return false;
        }

        if (descricaoNormalizada.length() < 50) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Descrição deve ter pelo menos 50 caracteres."));
            Logging.d(TAG, "Descrição muito curta");
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

    public List<Denuncia> listarDenunciasPorUsuario(Usuario usuario) {
        return read("select d from Denuncia d where d.usuario.id = " + usuario.getId(), Denuncia.class);
    }

    public List<Denuncia> getTodasDenuncias() {
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
