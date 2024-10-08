package com.pdsc.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@SuppressWarnings("SerializableClass")
@Entity
public class Denuncia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Size(min = 2, max = 50)
    private String setorDenuncia;

    @Size(min = 2, max = 50)
    private String tipoDenuncia;

    @Size(min = 2, max = 100)
    private String assundoDenuncia;

    @Temporal(TemporalType.DATE)
    private Date dataDenuncia;

    @Size(min = 2, max = 100)
    private String localDenuncia;

    @Size(min = 5)
    private String descricaoDenuncia;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Pattern(regexp = "[N|P|E|R|A]", message = "Estado da denúncia inválido")
    private String estadoDenuncia; /* novo N | processando P | encaminhado E | resolvido R | arquivado A */

    @ManyToOne
    private Servidor servidor;

    @ManyToOne
    private Usuario usuario;

    public int getId() {
        return id;
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

    public String getSetorDenuncia() {
        return setorDenuncia;
    }

    public void setSetorDenuncia(String setorDenuncia) {
        this.setorDenuncia = setorDenuncia;
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