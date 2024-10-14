package com.pdsc.model;

import com.pdsc.util.Logging;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 *
 * @author Willian Santos
 */

@Entity
public class Denuncia implements Serializable { 
    
    @Transient
    private static final String TAG = Denuncia.class.getSimpleName();
    @Transient
    public static final String NOVA = "Novo";
    @Transient
    public static String PROCESSAMENTO = "Em processamento";
    @Transient
    public static String ENCAMINHADO = "encaminhado";
    @Transient
    public static String RESOLVIDO = "resolvido";
    @Transient
    public static String ARQUIVADO = "Arquivado";
    @Transient
    public static String AGUARDANDO = "Aguardando informação";
    @Transient
    public static String INVALIDO = "Invalido";
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Size(min = 2, max = 50)
    @NotNull
    @Column(length=50, nullable = false, name = "TIPO_DENUNCIA")
    private String tipoDenuncia;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(length=50, nullable = false, name = "ASSUNTO_DENUNCIA")
    private String assundoDenuncia;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "DATA_DENUNCIA")
    private Date dataDenuncia;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(length=50, nullable = false, name = "LOCAL_DENUNCIA")
    private String localDenuncia;

    @NotNull
    @Column(length=5500, nullable = false, name = "DESCRICAO_DENUNCIA")
    private String descricaoDenuncia;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(nullable = false, name = "DATA_CRIACAO_DENUNCIA")
    private Date dataCriacao;

    // novo N | processando P | encaminhado E | resolvido R | arquivado A | I aguardando informação
    @Pattern(regexp = "[N|P|E|R|A|I]", message = "Estado da denúncia inválido")
    @NotNull
    @Size(min = 1, max = 1)
    @Column(length=1, nullable = false, name = "ESTADO_DENUNCIA")
    private String estadoDenuncia;
    
    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InformacaoAdicional> informacoesAdicionais = new ArrayList<>();

    @ManyToOne
    @NotNull
    @JoinColumn(name = "SERVIDOR_ID", nullable = false)
    private Servidor servidor;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;
    
    @Transient
    private String estadoDenunciaAmigavel;
    
    @Transient
    private String descricaoCurta;

    public int getId() {
        return id;
    }

    public List<InformacaoAdicional> getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(List<InformacaoAdicional> informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }
    
    public boolean canAddAditionalInfo() {
        return AGUARDANDO.equals(getEstadoDenunciaAmigavel())  || 
                ENCAMINHADO.equals(getEstadoDenunciaAmigavel())  ||
                PROCESSAMENTO.equals(getEstadoDenunciaAmigavel())  ||
                NOVA.equals(getEstadoDenunciaAmigavel());
    }

    public String getDescricaoCurta(){
        if (descricaoDenuncia.length() > 50) {
            return descricaoDenuncia.substring(0, 50) + "...";
        } else {
            return descricaoDenuncia;
        }
    }
    
    public void setDescricaoCurta() {
        if (descricaoDenuncia.length() > 50) {
            descricaoCurta = descricaoDenuncia.substring(0, 50) + "...";
        } else {
            descricaoCurta = descricaoDenuncia;
        }
    }
    
    public String getEstadoDenunciaAmigavel() {
        switch(estadoDenuncia) {
            case "N":
                return NOVA;
            case "P":
                return PROCESSAMENTO;
            case "E":
                return ENCAMINHADO;
            case "R":
                return RESOLVIDO;
            case "A":
                return ARQUIVADO;
            case "I":
                return AGUARDANDO;
            default:
                return INVALIDO;
        }
    }
    
    public void setEstadoDenunciaAmigavel(String estado) {
        switch(estado) {
            case "N":
                estadoDenunciaAmigavel = NOVA;
                break;
            case "P":
                estadoDenunciaAmigavel =  PROCESSAMENTO;
                break;
            case "E":
                estadoDenunciaAmigavel = ENCAMINHADO;
                break;
            case "R":
                estadoDenunciaAmigavel =  RESOLVIDO;
                break;
            case "A":
                estadoDenunciaAmigavel =  ARQUIVADO;
                break;
            case "I":
                estadoDenunciaAmigavel = AGUARDANDO;
            default:
                estadoDenunciaAmigavel =  INVALIDO;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataDenuncia() {
        return dataDenuncia;
    }

    public void setDataDenuncia(Date dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }

    public String getLocalDenuncia() {
        return localDenuncia;
    }

    public void setLocalDenuncia(String localDenuncia) {
        this.localDenuncia = localDenuncia;
    }

    public String getDescricaoDenuncia() {
        return descricaoDenuncia;
    }

    public void setDescricaoDenuncia(String descricaoDenuncia) {
        this.descricaoDenuncia = descricaoDenuncia;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipoDenuncia() {
        return tipoDenuncia;
    }

    public void setTipoDenuncia(String tipoDenuncia) {
        this.tipoDenuncia = tipoDenuncia;
    }

    public String getAssundoDenuncia() {
        return assundoDenuncia;
    }

    public void setAssundoDenuncia(String assundoDenuncia) {
        this.assundoDenuncia = assundoDenuncia;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEstadoDenuncia() {
        return estadoDenuncia;
    }
    
    public void setEstadoDenuncia(String estadoDenuncia) {
        this.estadoDenuncia = estadoDenuncia;
    }
    
}