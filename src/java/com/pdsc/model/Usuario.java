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
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Willian Santos
 */

@Entity
public class Usuario  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_USUARIO")
    private int id;

    @NotBlank(message = "Matrícula não pode estar em branco.")
    @Size(min = 4, max = 10, message = "A matrícula deve ter entre 4 e 10 caracteres.")
    @NotNull
    @Column(length=10, nullable = false, name = "MATRICULA_USUARIO")
    private String matricula;

    @NotBlank(message = "Nome não pode estar em branco.")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
    @NotNull
    @Column(length=50, nullable = false, name = "NOME_USUARIO")
    private String nome;

    @NotBlank(message = "Senha não pode estar em branco.")
    @NotNull
    @Column(length=300, nullable = false, name = "SENHA_USUARIO")
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Denuncia> denuncias = new ArrayList();

    
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
        return "Usuario{" + "id=" + id + ", matricula=" + matricula + ", nome=" + nome + ", senha=" + senha + ", denuncias=" + denuncias + '}';
    }
    
    
}