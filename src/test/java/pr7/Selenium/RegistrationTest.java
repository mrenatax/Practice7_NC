package pr7.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.*;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RegistrationTest {
    private static WebDriver driver;
    private static final String webFormURL = "https://inventory.edu-netcracker.com/pages/registration.xhtml";
    private static final String httpURL = "http://inventory.edu-netcracker.com/pages/registration.xhtml";
    private static final String registeredURL = "https://inventory.edu-netcracker.com/login.jsp?justRegistered=true";
    private static final String registerBtn = "registerForm:j_idt26";
    private static final String loginField = "registerForm:username";
    private static final String passwdField = "registerForm:password";
    private static final String confirmPasswdField = "registerForm:confirmPassword";
    private static final String email = "registerForm:email";
    private static final String role = "registerForm:role";
    private static final String hidePassword = "registerForm:hide";
    private static final String loginError = "//form[@id='registerForm']/table/tbody/tr/td[3]/span";
    private static final String passwordError = "//form[@id='registerForm']/table/tbody/tr[2]/td[3]/span";
    private static final String emailError = "//form[@id='registerForm']/table/tbody/tr[5]/td[3]/span";
    private static final String registeredEmail = "mrenatax.rd@gmail.com";
    private static final String registeredLogin = "Userr2";

    @BeforeClass
    public static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        System.setProperty("webdriver.chrome.driver", "");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /**
     * Method for filling fields
     */
    public void fillTheForm(String name, String password, String confPassword, String mail, String userRole, int ts, int tc) throws IOException {
        driver.get(webFormURL);
        String pathName = "screenshots\\TS" + ts + "\\" + "TC" + tc + "_";
        takeScreenshot(pathName + "Step0" + ".jpeg");
        driver.findElement(By.id(loginField)).sendKeys(name);
        takeScreenshot(pathName + "Step1" + ".jpeg");
        driver.findElement(By.id(passwdField)).sendKeys(password);
        takeScreenshot(pathName + "Step2" + ".jpeg");
        driver.findElement(By.id(confirmPasswdField)).sendKeys(confPassword);
        takeScreenshot(pathName + "Step3" + ".jpeg");
        driver.findElement(By.id(email)).sendKeys(mail);
        takeScreenshot(pathName + "Step4" + ".jpeg");
        driver.findElement(By.id(role)).click();
        new Select(driver.findElement(By.id(role))).selectByVisibleText(userRole);
        takeScreenshot(pathName + "Step5" + ".jpeg");
        driver.findElement(By.name(registerBtn)).click();
        takeScreenshot(pathName + "Step6" + ".jpeg");
    }

    /**
     * TC_1 New user registration with different roles
     */
    @Test
    public void testRoleRegistration() throws IOException {
        //Admin role TC_1.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), mailGenerator(), Role.ADMIN.getRole(), 1, 1);
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Read-only role TC_1.2
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user3_password"),
                ConfProperties.getProperty("user3_password2"), mailGenerator(), Role.READ_ONLY.getRole(), 1, 2);
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Read-write role TC_1.3
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user4_password"),
                ConfProperties.getProperty("user4_password2"), mailGenerator(), Role.READ_WRITE.getRole(), 1, 3);
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_2 Checking the "Username" textbox
     */
    @Test
    public void testUsername() throws IOException {
        //valid username TC_2.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole(), 2, 1);
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Registration with username which was already registered TC_2.2
        fillTheForm(registeredLogin, ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole(), 2, 2);
        assertEquals(ErrorMessage.REGISTERED_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());

        //Registration with username which length less than 6 TC_2.3
        fillTheForm(ConfProperties.getProperty("user1_name"), ConfProperties.getProperty("user1_password"),
                ConfProperties.getProperty("user1_password2"), ConfProperties.getProperty("user1_email"), Role.READ_WRITE.getRole(), 2, 3);
        assertEquals(ErrorMessage.INVALID_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());

        //Registration with username which contains non alphanumeric symbols TC_2.4
        fillTheForm(ConfProperties.getProperty("user11_name"), ConfProperties.getProperty("user11_password"),
                ConfProperties.getProperty("user11_password2"), ConfProperties.getProperty("user11_email"), Role.READ_WRITE.getRole(), 2, 4);
        assertEquals(ErrorMessage.INVALID_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());
    }

    /**
     * TC_3 Checking the "Password" textbox
     */
    @Test
    public void testPassword() throws IOException {
        //Valid data for "Password" textbox TC_3.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole(), 3, 1);
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Registration with password which length less than 8 TC_3.2
        fillTheForm(ConfProperties.getProperty("user12_name"), ConfProperties.getProperty("user12_password"),
                ConfProperties.getProperty("user12_password2"), ConfProperties.getProperty("user12_email"), Role.READ_WRITE.getRole(), 3, 2);
        assertEquals(ErrorMessage.PASSWORD_LENGTH.getMessage(), driver.findElement(By.xpath(passwordError)).getText());

        //Registration with password that doesn't contain at least one uppercase letter TC_3.3
        fillTheForm(ConfProperties.getProperty("user13_name"), ConfProperties.getProperty("user13_password"),
                ConfProperties.getProperty("user13_password2"), ConfProperties.getProperty("user13_email"), Role.READ_WRITE.getRole(), 3, 3);
        assertEquals(ErrorMessage.PASSWORD_UPPER_LETTER.getMessage(), driver.findElement(By.xpath(passwordError)).getText());

        //Registration with password that doesn't contain at least one non alphanumeric symbol TC_3.4
        fillTheForm(ConfProperties.getProperty("user14_name"), ConfProperties.getProperty("user14_password"),
                ConfProperties.getProperty("user14_password2"), ConfProperties.getProperty("user14_email"), Role.READ_WRITE.getRole(), 3, 4);
        assertEquals(ErrorMessage.PASSWORD_ALPHANUMERIC.getMessage(), driver.findElement(By.xpath(passwordError)).getText());

        //Registration with password that wasn't correctly confirmed TC_3.5
        fillTheForm(ConfProperties.getProperty("user16_name"), ConfProperties.getProperty("user16_password"),
                ConfProperties.getProperty("user16_password2"), ConfProperties.getProperty("user16_email"), Role.READ_WRITE.getRole(), 3, 5);
        assertEquals(ErrorMessage.PASSWORD_MATCH.getMessage(), driver.findElement(By.xpath(passwordError)).getText());
    }

    /**
     * TC_4 Checking the "Email" textbox
     */
    @Test
    public void testEmail() throws IOException {
        //Valid data for "Email" textbox TC_4.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole(), 4, 1);
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Registration with invalid email TC_4.2
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user15_password"),
                ConfProperties.getProperty("user15_password2"), ConfProperties.getProperty("user15_email"), Role.READ_WRITE.getRole(), 4, 2);
        assertEquals(ErrorMessage.INVALID_EMAIL.getMessage(), driver.findElement(By.xpath(emailError)).getText());

        //Registration with already registered email TC_4.3
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), registeredEmail, Role.READ_WRITE.getRole(), 4, 3);
        assertEquals(webFormURL, driver.getCurrentUrl()); // тест не проходит, т.к. функционал не соответствует документации
    }

    /**
     * TC_5 Checking the fields after incorrect input
     */
    @Test
    public void testFields() throws IOException {
        //All fields remain filled after incorrect input
        fillTheForm(ConfProperties.getProperty("user11_name"), ConfProperties.getProperty("user11_password"),
                ConfProperties.getProperty("user11_password2"), ConfProperties.getProperty("user11_email"), Role.ADMIN.getRole(), 5, 1);
        assertEquals(ConfProperties.getProperty("user11_name"), driver.findElement(By.id(loginField)).getAttribute("value"));
        assertEquals(ConfProperties.getProperty("user11_email"), driver.findElement(By.id(email)).getAttribute("value"));
        assertEquals(Role.ADMIN.getRoleAsAttribute(), driver.findElement(By.id(role)).getAttribute("value"));
    }

    /**
     * TC_6 Checking the availability of the form using the https protocol
     */
    @Test
    public void testURL() throws IOException {
        driver.get(httpURL);
        takeScreenshot("screenshots\\TS6\\TC1_Step1.jpeg");
        assertEquals(webFormURL, driver.getCurrentUrl());
    }

    /**
     * TC_7-8
     */
    @Test
    public void testPasswordHiding() throws IOException {
        //Checking each entered password character is hidden by stars
        driver.get(webFormURL);
        takeScreenshot("screenshots\\TS8\\TC1_Step0.jpeg");
        driver.findElement(By.id(passwdField)).sendKeys("Password1+");
        takeScreenshot("screenshots\\TS8\\TC1_Step1.jpeg");
        driver.findElement(By.id(confirmPasswdField)).sendKeys("Password1+");
        takeScreenshot("screenshots\\TS8\\TC1_Step2.jpeg");
        assertEquals("password", driver.findElement(By.id(passwdField)).getAttribute("type"));
        driver.findElement(By.id(hidePassword)).click();
        takeScreenshot("screenshots\\TS8\\TC1_Step3.jpeg");
        assertEquals("text", driver.findElement(By.id(passwdField)).getAttribute("type"));
    }

    /**
     * TC_10 Checking access to the system
     */
    @Test
    public void testAccessWithoutConfirmation() throws IOException {
        //Checking access to the system by a user who has not confirmed registration
        String login = loginGenerator();
        String mail = mailGenerator();
        fillTheForm(login, ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), mail, Role.ADMIN.getRole(), 10, 1);
        driver.findElement(By.name("j_username")).sendKeys(login);
        takeScreenshot("screenshots\\TS10\\TC1_Step7.jpeg");
        driver.findElement(By.name("j_password")).sendKeys(ConfProperties.getProperty("user2_password"));
        takeScreenshot("screenshots\\TS10\\TC1_Step8.jpeg");
        driver.findElement(By.name("submit")).click();
        takeScreenshot("screenshots\\TS10\\TC1_Step9.jpeg");
        assertEquals(registeredURL, driver.getCurrentUrl()); //пользователь получает доступ без email подтверждения (несоответствие документации)
    }

    public void takeScreenshot(String pathname) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(pathname));
    }

    public String loginGenerator() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");
    }

    public String mailGenerator() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz") + "@" + "testmail.com";
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}