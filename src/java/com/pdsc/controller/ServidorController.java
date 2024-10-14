package com.pdsc.controller;

import com.pdsc.model.Servidor;
import com.pdsc.util.CriptoHelper;
import com.pdsc.util.Logging;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;


@ManagedBean
@SessionScoped
public class ServidorController extends Controller {

    private static final String TAG = ServidorController.class.getSimpleName();
    private Servidor servidorCadastro;
    private Servidor selection;

    @PostConstruct
    public void init() {
        this.servidorCadastro = new Servidor();
        this.selection = new Servidor();
    }

    public void inserir(String confirma) {
        Logging.d(TAG, "inserir servidor");
        FacesContext context = FacesContext.getCurrentInstance();
        if (!servidorCadastro.getSenha().equals(confirma)) {
            context.addMessage("formRegistroServidor:senha",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "A senha e a confirmação não são iguais!"));
            return;
        }

        Logging.d(TAG, "senhas conferem");
        if(!ServidorEValido(servidorCadastro)){
            context.addMessage("formRegistroServidor:senha",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "servidor nao é valido!"));
            return;
        }
        
        Logging.d(TAG, "Servidor é valido");
        if (ePrimeiroServidor()) {
            Logging.d(TAG, "Primeiro servidor: ADM");
            servidorCadastro.setFuncao(Servidor.ADM);
        }
        
        try {
            String senhaCriptografada = CriptoHelper.gerarHash256(confirma);
            servidorCadastro.setSenha(senhaCriptografada);
            insert(servidorCadastro);
            servidorCadastro = new Servidor();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Servidor cadastrado com sucesso!"));
            
        }catch (javax.validation.ConstraintViolationException e) {
            // Exibe todas as violações detectadas
            e.getConstraintViolations().forEach((violation) -> {
                Logging.d(TAG, "Violação: " + violation.getPropertyPath() +
                        " - " + violation.getMessage() +
                        " (Valor inválido: " + violation.getInvalidValue() + ")");
            });
            throw e; // Repassa a exceção para tratamento em nível superior
        }catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível cadastrar o servidor."));
            Logging.d(TAG, "erro ao inserir servidor " + e.getMessage());
        }
    }
    
    
    private boolean ServidorEValido(Servidor s) {
        Logging.d(TAG, "ServidorEValido()");
        if (s == null) {
            Logging.d(TAG, "Servidor é nulo.");
                return false;
            }

        // Validação da matrícula
        if (s.getMatricula() == null || s.getMatricula().isEmpty()) {
            Logging.d(TAG, "Matrícula não pode estar em branco.");
            return false;
        }
        if (s.getMatricula().length() < 4 || s.getMatricula().length() > 10) {
            Logging.d(TAG, "Matrícula deve ter entre 4 e 10 caracteres.");
            return false;
        }

        // Validação do nome
        if (s.getNome() == null || s.getNome().isEmpty()) {
            Logging.d(TAG, "Nome não pode estar em branco.");
            return false;
        }
        if (s.getNome().length() < 2 || s.getNome().length() > 50) {
            Logging.d(TAG, "Nome deve ter entre 2 e 50 caracteres.");
            return false;
        }

        // Validação do cargo
        if (s.getCargo() == null || s.getCargo().isEmpty()) {
            Logging.d(TAG, "Cargo não pode estar em branco.");
            return false;
        }
        if (s.getCargo().length() < 2 || s.getCargo().length() > 30) {
            Logging.d(TAG, "Cargo deve ter entre 2 e 30 caracteres.");
            return false;
        }

        // Validação do setor
        if (s.getSetor() == null || s.getSetor().isEmpty()) {
            Logging.d(TAG, "Setor não pode estar em branco.");
            return false;
        }
        if (s.getSetor().length() < 2 || s.getSetor().length() > 30) {
            Logging.d(TAG, "Setor deve ter entre 2 e 30 caracteres.");
            return false;
        }

        // Validação da senha
        if (s.getSenha() == null || s.getSenha().isEmpty()) {
            Logging.d(TAG, "Senha não pode estar em branco.");
            return false;
        }
        if (!s.getSenha().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{6,20}$")) {
            Logging.d(TAG, "Senha inválida: Deve ter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.");
            return false;
        }

        // Validação da função
        if (s.getFuncao() == null || s.getFuncao().isEmpty()) {
            Logging.d(TAG, "Função não pode estar em branco.");
            return false;
        }
        if (s.getFuncao().length() < 6 || s.getFuncao().length() > 20) {
            Logging.d(TAG, "Função deve ter entre 6 e 20 caracteres.");
            return false;
        }
        
        // Validação da função usando um array fixo
        String[] funcoesPermitidas = {Servidor.ADM, Servidor.TRIAGEM, Servidor.SUPERVISOR};
        boolean funcaoValida = false;
        for (String funcao : funcoesPermitidas) {
            if (funcao.equals(s.getFuncao())) {
                funcaoValida = true;
                break;
            }
        }
        
        if (!funcaoValida) {
            Logging.d("ServidorEValido", "Função inválida: Deve ser uma das seguintes: Administrador, Triagem ou Supervisor.");
            return false;
        }

        // Validação das denúncias (opcional)
        if (s.getDenuncias() == null) {
            Logging.d(TAG, "A lista de denúncias não pode ser nula.");
            return false;
        }

        Logging.d(TAG, "ServidorEValido(): true");
        return true;
    }
    
    private boolean ePrimeiroServidor() {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = (Long) em.createQuery("SELECT COUNT(s) FROM Servidor s").getSingleResult();
            return count == 0;
        } catch (Exception e) {
            Logging.d(TAG, "erro em verificar o banco " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos os servidores do banco de dados.
     * 
     * @return Lista de servidores.
     */
    public List<Servidor> readAll() {
        try {
            return read("select p from Servidor p", Servidor.class);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível recuperar a lista de servidores."));
            e.printStackTrace(); // Log de erro para diagnóstico
            return null;
        }
    }

    // Getters e Setters
    public Servidor getServidorCadastro() {
        return servidorCadastro;
    }

    public void setServidorCadastro(Servidor servidorCadastro) {
        this.servidorCadastro = servidorCadastro;
    }

    public Servidor getSelection() {
        return selection;
    }

    public void setSelection(Servidor selection) {
        this.selection = selection;
    }
}
