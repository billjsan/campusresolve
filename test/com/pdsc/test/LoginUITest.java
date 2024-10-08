package com.pdsc.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertTrue;

public class LoginUITest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Configurar o caminho para o ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:/Users/david/Downloads/Selenium/CHROME/chromedriver-win64/chromedriver.exe");

        // Inicializar o WebDriver
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        // Fechar o navegador
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRegistrarUsuarioDadosValidos() throws InterruptedException {
        // Navegar para a página de login
        driver.get("http://localhost:8080/CampusResolve/faces/login.xhtml");

        // Adicionar delay para visualização
        Thread.sleep(2000); // Espera por 2 segundos

        // Preencher os campos de login
        driver.findElement(By.id("input_j_idt6:j_idt7:j_idt10")).sendKeys("12345");
        Thread.sleep(1000); // Espera por 1 segundo

        driver.findElement(By.id("input_j_idt6:j_idt7:j_idt12")).sendKeys("Testeifpe2*");
        Thread.sleep(1000); // Espera por 1 segundo

        // Clicar no botão de login
        driver.findElement(By.id("j_idt6:j_idt7:j_idt13")).click();
        Thread.sleep(2000); // Espera por 2 segundos

        // Verificar se o login foi bem-sucedido verificando o conteúdo da página
        WebElement spanElement = driver.findElement(By.tagName("span"));
        String pageContent = spanElement.getText();
        assertTrue(pageContent.contains("Primefaces welcome page"));

        // Chamar o método usuarioBasico para testar a navegação
        usuarioBasico();
    }

    public void usuarioBasico() throws InterruptedException {

        // Navegar para a página de login
        driver.get("http://localhost:8080/CampusResolve/faces/login.xhtml");
        // Clicar no botão para entrar na tela de Usuário Básico
        driver.findElement(By.id("j_idt17_tab")).click();
        Thread.sleep(3000); // Espera por 3 segundos para carregar a página

        // Adicionar delay para visualização
        Thread.sleep(2000); // Espera por 2 segundos

        // Preencher os campos de login
        driver.findElement(By.id("input_j_idt17:j_idt18:j_idt21")).sendKeys("1234");
        Thread.sleep(1000); // Espera por 1 segundo

        driver.findElement(By.id("input_j_idt17:j_idt18:j_idt23")).sendKeys("Testeifpe1*");
        Thread.sleep(1000); // Espera por 1 segundo

        // Clicar no botão de login
        driver.findElement(By.id("j_idt17:j_idt18:j_idt24")).click();
        Thread.sleep(2000); // Espera por 2 segundos

        // Verificar se o login foi bem-sucedido identificando o formulário "formMenu"
        WebElement formElement = driver.findElement(By.id("formMenu"));
        assertTrue(formElement.isDisplayed()); // Verifica se o formulário está sendo exibido

    }

    @Test
    public void testLoginUsuarioInvalido() throws InterruptedException {
        // Fluxo de login inválido
        driver.get("http://localhost:8080/CampusResolve/faces/login.xhtml");
        Thread.sleep(3000); // Espera por 2 segundos
        driver.findElement(By.id("input_j_idt6:j_idt7:j_idt10")).sendKeys("99999");
        Thread.sleep(2000); // Espera por 2 segundos
        driver.findElement(By.id("input_j_idt6:j_idt7:j_idt12")).sendKeys("SenhaErrada");
        Thread.sleep(2000); // Espera por 2 segundos
        driver.findElement(By.id("j_idt6:j_idt7:j_idt13")).click();
        Thread.sleep(2000);

        WebElement errorMessage = driver.findElement(By.cssSelector(".col-xs-11.col-sm-4.alert.alert-danger"));
        String actualErrorMessage = errorMessage.getText();
        assertTrue(actualErrorMessage.contains("Erro ao Logar Matricula e/ou senha estão incorretos"));
    }

    @Test
    public void testCadastroServidorValido() throws InterruptedException {
        // Fluxo de login inválido
        driver.get("http://localhost:8080/CampusResolve/faces/login.xhtml");
        Thread.sleep(3000); // Espera por 2 segundos
        driver.findElement(By.id("j_idt6:j_idt7:j_idt16")).click();
        Thread.sleep(2000); // Espera por 2 segundos
        driver.findElement(By.id("input_formRegistroServidor:j_idt30")).sendKeys("123456");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroServidor:j_idt32")).sendKeys("servidorTest");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroServidor:j_idt34")).sendKeys("ptofessorTest");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroServidor:j_idt36")).sendKeys("adsTeste");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroServidor:j_idt38")).sendKeys("Testeifpe5!");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroServidor:j_idt40")).sendKeys("Testeifpe5!");
        Thread.sleep(2000); // Espera por 2 segundos
        driver.findElement(By.id("formRegistroServidor:j_idt41")).click();
        Thread.sleep(2000);

        // Localiza o span com data-notify="title"
        WebElement titleSpan = driver.findElement(By.cssSelector("span[data-notify='title']"));
        String titleText = titleSpan.getText();
        System.out.println("Texto do titulo: " + titleText);

    }

    @Test
    public void testCadastroUsuario() throws InterruptedException {
        // Fluxo de login inválido
        driver.get("http://localhost:8080/CampusResolve/faces/login.xhtml");
        // Clicar no botão para entrar na tela de Usuário Básico
        driver.findElement(By.id("j_idt17_tab")).click();
        Thread.sleep(2000); // Espera por 2 segundos
        driver.findElement(By.id("j_idt17:j_idt18:j_idt27")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroUsuario:j_idt44")).sendKeys("54321");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroUsuario:j_idt46")).sendKeys("usuarioTest");
        Thread.sleep(2000);;
        driver.findElement(By.id("input_formRegistroUsuario:j_idt48")).sendKeys("Testeifpe5*");
        Thread.sleep(2000);
        driver.findElement(By.id("input_formRegistroUsuario:j_idt50")).sendKeys("Testeifpe5*");
        Thread.sleep(2000); // Espera por 2 segundos
        driver.findElement(By.id("formRegistroUsuario:j_idt51")).click();
        Thread.sleep(2000);

        // Localiza o span com data-notify="title"
        WebElement titleSpan = driver.findElement(By.cssSelector("span[data-notify='title']"));
        String titleText = titleSpan.getText();
        System.out.println("Texto do titulo: " + titleText);

    }

}
