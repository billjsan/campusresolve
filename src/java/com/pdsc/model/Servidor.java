package com.pdsc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Willian Santos
 */

@Entity
public class Servidor implements Serializable {

    @Transient
    public static final String ADM = "Administrador";
    @Transient
    public static final String TRIAGEM = "Triagem";
    @Transient
    public static final String SUPERVISOR = "Supervisor";
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_SERVIDOR")
    private int id;

    @NotBlank(message = "Matrícula não pode estar em branco.")
    @Size(min = 4, max = 10, message = "A matrícula deve ter entre 4 e 10 caracteres.")
    @NotNull
    @Column(length=10, nullable = false, name = "MATRICULA_SERVIDOR")
    private String matricula;

    @NotBlank(message = "Nome não pode estar em branco.")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
    @NotNull
    @Column(length=50, nullable = false, name = "NOME_SERVIDOR")
    private String nome;

    @NotBlank(message = "Cargo não pode estar em branco.")
    @Size(min = 2, max = 30, message = "O cargo deve ter entre 2 e 30 caracteres.")
    @NotNull
    @Column(length=30, nullable = false, name = "CARGO_SERVIDOR")
    private String cargo;

    @NotBlank(message = "Setor não pode estar em branco.")
    @Size(min = 2, max = 30, message = "O setor deve ter entre 2 e 30 caracteres.")
    @NotNull
    @Column(length=30, nullable = false, name = "SETOR_SERVIDOR")
    private String setor;

    @NotBlank(message = "Senha não pode estar em branco.")
    
    @Column(length=300, nullable = false, name = "SENHA_SERVIDOR")
    private String senha;
    
    @NotBlank(message = "A função não pode ser vazia")
    @Size(min = 6, max = 20, message = "A função deve ter entre 6 e 20 caracteres")
    @Column(length=20, nullable = false, name = "FUNCAO_SERVIDOR")
    private String funcao = TRIAGEM;

    @OneToMany(mappedBy = "servidor", cascade = CascadeType.ALL)
    private List<Denuncia> denuncias = new ArrayList<>();

    public String getFuncao() {
        return funcao;
    }
    
    public void setFuncao(String func) {
        funcao =  func;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Denuncia> getDenuncias() {
        return denuncias;
    }

    public void setDenuncias(List<Denuncia> denuncias) {
        this.denuncias = denuncias;
    }

    @Override
    public String toString() {
        return "Servidor{" + "id=" + id + ", matricula=" + matricula + ", nome=" + nome + ", cargo=" + cargo + ", setor=" + setor + ", senha=" + senha + ", funcao=" + funcao + ", denuncias=" + denuncias + '}';
    }
    
    
}
