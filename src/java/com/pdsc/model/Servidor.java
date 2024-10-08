package com.pdsc.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;


@Entity
public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotBlank(message = "Matrícula não pode estar em branco.")
    @Size(min = 4, max = 10, message = "A matrícula deve ter entre 4 e 10 caracteres.")
    private String matricula;

    @NotBlank(message = "Nome não pode estar em branco.")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
    private String nome;

    @NotBlank(message = "Cargo não pode estar em branco.")
    @Size(min = 2, max = 30, message = "O cargo deve ter entre 2 e 30 caracteres.")
    private String cargo;

    @NotBlank(message = "Setor não pode estar em branco.")
    @Size(min = 2, max = 30, message = "O setor deve ter entre 2 e 30 caracteres.")
    private String setor;

    @NotBlank(message = "Senha não pode estar em branco.")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{6,20}$", 
             message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial.")
    private String senha;

    @OneToMany(mappedBy = "Servidor", cascade = CascadeType.ALL)
    private List<Denuncia> denuncias;

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
}
