package com.pdsc.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.pdsc.model.Denuncia;
import com.pdsc.model.InformacaoAdicional;
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
import java.util.HashMap;
import java.util.Map;
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
    private String infoAdicionalString;
    private String infoAdicionalNova = "";
    private String nomeCriadorInfoAdicional = "";

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
    
    public void atualizarStatusDenuncia() {
        try {
            update(denunciaSelecionada);
        } catch (Exception e) {
            Logging.d(TAG, "erro em atualizarStatusDenuncia() " + e.getMessage());
        }
    }

    public String getInfoAdicionalNova() {
        return infoAdicionalNova;
    }

    public String getNomeCriadorInfoAdicional() {
        return nomeCriadorInfoAdicional;
    }

    public void setNomeCriadorInfoAdicional(String nomeCriadorInfoAdicional) {
        this.nomeCriadorInfoAdicional = nomeCriadorInfoAdicional;
    }

    public void setInfoAdicionalNova(String infoAdicionalNova) {
        this.infoAdicionalNova = infoAdicionalNova;
    }

    
    

    
    
    public List<Denuncia> getTodasDenunciasUsuario() {
        Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getUsuarioLogado();
            return read("SELECT d FROM Denuncia d WHERE d.usuario.id = " + usuarioLogado.getId(), Denuncia.class);
    }
    
    private boolean usuarioEstaLogado() {
        Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                            .getExternalContext().getSession(true))
                            .getAttribute("loginController")).getUsuarioLogado();
                                    return usuarioLogado!= null;
    
    }
    
    private boolean servidorEstaLogado() {
        Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getServidorLogado();
         return servidorLogado!= null;
    }
    
    public void atualizarInfoAdicional() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(denunciaSelecionada == null || infoAdicionalNova == null || infoAdicionalNova.isEmpty()) {
            Logging.d(TAG, "erro em atualizarInfoAdicional() ");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "erro ao atualizarInfoAdicional"));
                return ;
        }

        try {
            Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getUsuarioLogado();

            Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getServidorLogado();
            
            if (usuarioLogado == null && servidorLogado == null) {
                Logging.d(TAG, "erro nenhum usuario ou servidor logado foi encontrado");
                return;
            }
            
            InformacaoAdicional info = new InformacaoAdicional();
            if(usuarioLogado != null) {
                Logging.d(TAG, "usuario logado encontrado");
                info.setIdCriador(usuarioLogado.getId());
                info.setTipoCriador(InformacaoAdicional.USUARIO);
                info.setNomeCriador(usuarioLogado.getNome());
                
            } else {
                Logging.d(TAG, "servidor logado encontrado");
                info.setIdCriador(servidorLogado.getId());
                info.setTipoCriador(InformacaoAdicional.SERVIDOR);
                info.setNomeCriador(servidorLogado.getNome());
            } 
            info.setInformacao(infoAdicionalNova);
            info.setDenuncia(denunciaSelecionada);
            infoAdicionalNova = "";
            denunciaSelecionada.getInformacoesAdicionais().add(info);
            update(denunciaSelecionada);
        } catch (Exception e) {
            Logging.d(TAG, "erro em inserirInfoAdicional() " + e.getMessage());
        }
    }

    public void inserirInfoAdicional() {
        Logging.d(TAG, "inserirInfoAdicional()");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getUsuarioLogado();

            Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getServidorLogado();
            
            if (usuarioLogado == null && servidorLogado == null) {
                Logging.d(TAG, "erro nenhum usuario ou servidor logado foi encontrado");
                return;
            }
            
            InformacaoAdicional info = new InformacaoAdicional();
            if(usuarioLogado != null) {
                Logging.d(TAG, "usuario logado encontrado");
                info.setIdCriador(usuarioLogado.getId());
                info.setTipoCriador(InformacaoAdicional.USUARIO);
                
            } else {
                Logging.d(TAG, "servidor logado encontrado");
                info.setIdCriador(servidorLogado.getId());
                info.setTipoCriador(InformacaoAdicional.SERVIDOR);
            } 
            info.setInformacao(infoAdicionalString);
            denunciaSelecionada.getInformacoesAdicionais().add(info);
            update(denunciaSelecionada);
        } catch (Exception e) {
            Logging.d(TAG, "erro em inserirInfoAdicional() " + e.getMessage());
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
        return read("select d from Denuncia d where d.usuario.id = " + usuario.getId(), Denuncia.class); // PARECE QUE TEM UM ERRO AQUI
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
            try {
                update(denunciaSelecionada);
            } catch (Exception ex) {
               Logging.d(TAG, "atualizarDenuncia() erro");
            }
        }
    }

    public void excluirDenuncia() {
        if (denunciaSelecionada != null) {
            delete(denunciaSelecionada);
        }
    }
    
    public List<Denuncia> getTodasDenunciasParaClienteLogado() {
        Logging.d(TAG, "getTodasDenunciasParaClienteLogado");
        EntityManager em = emf.createEntityManager();
        List<Denuncia> result = new ArrayList();
        try {
            if(servidorEstaLogado()) {
                Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getServidorLogado();
                
                Servidor servidorDoBanco = (Servidor) read("select d from Servidor d where d.id = " + servidorLogado.getId(), Denuncia.class).get(0);
                if(servidorDoBanco != null) {
                    Logging.d(TAG, "servidorDoBanco: nome:" + servidorDoBanco.getNome() + " ID: " + servidorDoBanco.getId()  + " quantidade Denuncias: " + servidorDoBanco.getDenuncias().size());
                    result = servidorDoBanco.getDenuncias();
                }
            } else if(usuarioEstaLogado()) {
                Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getUsuarioLogado();
                Logging.d(TAG, "usuarioLogado: nome:" + usuarioLogado.getNome() + " ID: " + usuarioLogado.getId()  + " quantidade Denuncias: " + usuarioLogado.getDenuncias().size());
                result = usuarioLogado.getDenuncias();
            }
        } catch (Exception e) {
            Logging.d(TAG, "erro em getNumeroDenunciaPorTipoString() " + e.getMessage());
        } finally{
            em.close();  
        }
        return result;
    }
    
    /**
     * 
     * FUNCIONA COMO ESPERADO
     * 
     * @param tipo
     * @return 
     */
    public String getQuantidadeOcorrenciaPorTipoString(String tipo) {
        Logging.d(TAG, "getQuantidadeOcorrenciaPorTipoString(): tipo:" + tipo);
        String result = "0";
        try {
            if ("todas".equals(tipo)) {
                Logging.d(TAG, "tipo = todas");
                return String.valueOf(getTodasDenuncias().size());
            }
            EntityManager em = emf.createEntityManager();
            Long count = 0L;
            if(servidorEstaLogado()) {
                Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getServidorLogado();
                count = (Long) em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.tipoDenuncia = :tipoDenuncia AND d.servidor.id =:idServidor")
                        .setParameter("tipoDenuncia", tipo)
                        .setParameter("idServidor", servidorLogado.getId())
                        .getSingleResult();
                Logging.d(TAG, "servidorEstaLogado() denuncias filtradas:" + count);
            
            } else if(usuarioEstaLogado()) {
                Usuario usuarioLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getUsuarioLogado();
                count = (Long) em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.tipoDenuncia = :tipoDenuncia AND d.usuario.id =:idUsuario")
                            .setParameter("tipoDenuncia", tipo)
                            .setParameter("idUsuario", usuarioLogado.getId())
                            .getSingleResult();
                 Logging.d(TAG, "usuarioEstaLogado() denuncias filtradas:" + count);
            }
            em.close();
            result = count.toString();
        } catch (Exception e) {
            Logging.d(TAG, "erro em getNumeroDenunciaPorTipoString() " + e.getMessage());
        }
        return result;
    }
    
    
    /**
     * 
     * FUNCIONA COMO ESPERADO
     * 
     * @return 
     */
    public List<Denuncia> getTodasDenuncias() {
        Logging.d(TAG, "getTodasDenuncias()");
        if(servidorEstaLogado()) {
            Logging.d(TAG, "servidorEstaLogado()");
            return getTodasDenunciasServidor();
        } else if(usuarioEstaLogado()) {
            Logging.d(TAG, "usuarioEstaLogado()");
            return getTodasDenunciasUsuario();
        }
        return new ArrayList();
    }
    
    /**
     * 
     * FUNCIONA COMO ESPERADO
     * 
     * @return 
     */
    public List<Denuncia> getTodasDenunciasServidor() {
        Logging.d(TAG, "getTodasDenunciasServidor()");
        Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(true))
                        .getAttribute("loginController")).getServidorLogado();
        Logging.d(TAG,"getTodasDenunciasServidor: Servidor Logado ID: " + servidorLogado.getId());
        List<Denuncia> denuncias = read("select d from Denuncia d where d.servidor.id = " + servidorLogado.getId(), Denuncia.class);
        Logging.d(TAG,"denuncias size: " + denuncias.size());
        return denuncias;
    }
    
   /**
    * 
    * FUNCIONA COMO ESPERADO
    * 
    * @param filtro 
    */
    public void setFiltroDenuncia(String filtro) {
        Logging.d(TAG, "setFiltroDenuncia(): filtro:" + filtro);
        filtroDenuncia = filtro;
    }
    
    /**
     * 
     * FUNCIONA COMO ESPERADO
     * 
     * @return 
     */
    public String getFiltroDenuncia() {
        Logging.d(TAG, "getFiltroDenuncia(): filtro:" + filtroDenuncia);
        return filtroDenuncia;
    }
    
    /**
     * 
     * FUNCIONA COMO ESPERADO
     * 
     * @return 
     */
    public List<Denuncia> getDenunciaFiltradas() {
        Logging.d(TAG, "getDenunciaFiltradas()");
        if (filtroDenuncia == null || filtroDenuncia.isEmpty()) {
            Logging.d(TAG, "filtroDenuncia == null || filtroDenuncia.isEmpty()");
            return getTodasDenuncias();
        } 
        return getDenunciasPorTipo(filtroDenuncia);
    }
    
    /**
     * 
     * FUNCIONA COMO ESPERADO
     * 
     * @param tipo
     * @return 
     */
    public List<Denuncia> getDenunciasPorTipo(String tipo) {
        Logging.d(TAG, "getDenunciasPorTipo: tipo:" + tipo);
        List<Denuncia> result = new ArrayList();
        EntityManager em = emf.createEntityManager();
        try {
            Servidor servidorLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance()
                              .getExternalContext().getSession(true))
                              .getAttribute("loginController")).getServidorLogado();
            Logging.d(TAG, "servidorLogado: " + servidorLogado.getNome() + "  ID: " + servidorLogado.getId());
            result = (List<Denuncia>) em.createQuery("SELECT d FROM Denuncia d WHERE d.tipoDenuncia = :tipoDenuncia AND d.servidor.id =:idServidor")
                .setParameter("tipoDenuncia", tipo)
                .setParameter("idServidor", servidorLogado.getId())
                .getResultList();
            Logging.d(TAG, "Lista Denuncias size: " + result.size());
        } catch (Exception e) {
            Logging.d(TAG, "erro em filtrarDenuncias() " + e.getMessage());
        } finally{
            em.close();
        }
        return result;
    }
}
