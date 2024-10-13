
package com.pdsc.model;

import com.pdsc.util.Logging;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author williansantos
 */
@Entity
public class InformacaoAdicional implements Serializable {
    
    @Transient
    private static final String TAG = InformacaoAdicional.class.getSimpleName(); 
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_INFORMACAO_ADICIONAL")
    private Long Id;
    
    @Column(length=5500, nullable = false, name = "INFORMACAO_ADICIONAL")
    private String informacao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(nullable = false, name = "DATA_MODIFICACAO_INFORMACAO_ADICIONAL")
    private Date dataModificacao = new Date();
    
    @ManyToOne
    @JoinColumn(name = "DENUNCIA_ID", nullable = false)
    private Denuncia denuncia;
    
    @Transient
    private String dataModificacaoFormatada;

    public Long getId() {
        return Id;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public String getDataModificacaoFormatada() {
        if (dataModificacao != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dataModificacaoFormatada = formato.format(dataModificacao);
        } else {
            dataModificacaoFormatada = "?";
        }
        return dataModificacaoFormatada;
    }

    public void setDataModificacaoFormatada(String dataModificacaoFormatada) {
        if (dataModificacao != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dataModificacaoFormatada = formato.format(dataModificacao);
        } else {
            dataModificacaoFormatada = "?";
        }
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        if(informacao != null) {
            if(!informacao.equals(this.informacao)) {
            dataModificacao = new Date();
            this.informacao = informacao;
            }
        }
        Logging.d(TAG, "setInformacao() nulo ou igual a anterior, bypass");
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

}
