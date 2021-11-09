package pr7;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

public class RegistrationTest {
    private StringBuffer verificationErrors = new StringBuffer();
    private static WebDriver driver;
    private static final String webFormURL = "https://inventory.edu-netcracker.com/pages/registration.xhtml";
    private static final String httpURL = "http://inventory.edu-netcracker.com/pages/registration.xhtml";
    private static final String registeredURL = "https://inventory.edu-netcracker.com/login.jsp?justRegistered=true";
    private static final String loginedURL = "https://inventory.edu-netcracker.com/pages/startpage.xhtml";
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

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // полезно для ускорения тестов - не запускать визуальную часть браузера.
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
    public void fillTheForm(String name, String password, String confPassword, String mail, String userRole) {
        driver.get(webFormURL);
        driver.findElement(By.id(loginField)).sendKeys(name);
        driver.findElement(By.id(passwdField)).sendKeys(password);
        driver.findElement(By.id(confirmPasswdField)).sendKeys(confPassword);
        driver.findElement(By.id(email)).sendKeys(mail);
        driver.findElement(By.id(role)).click();
        new Select(driver.findElement(By.id(role))).selectByVisibleText(userRole);
        driver.findElement(By.name(registerBtn)).click();
    }

    /**
     * TC_1 New user registration with different roles
     */
    @Test
    public void testRoleRegistration() {
        //Admin role TC_1.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), mailGenerator(), Role.ADMIN.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Read-only role TC_1.2
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user3_password"),
                ConfProperties.getProperty("user3_password2"), mailGenerator(), Role.READ_ONLY.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Read-write role TC_1.3
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user4_password"),
                ConfProperties.getProperty("user4_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_2 Cheching the "Username" textbox
     */
    @Test
    public void testUsername() {
        //valid username TC_2.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Registration with username which was already registered TC_2.2
        fillTheForm(registeredLogin, ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.REGISTERED_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());

        //Registration with username which length less than 6 TC_2.3
        fillTheForm(ConfProperties.getProperty("user1_name"), ConfProperties.getProperty("user1_password"),
                ConfProperties.getProperty("user1_password2"), ConfProperties.getProperty("user1_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.INVALID_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());

        //Registration with username which contains non alphanumeric symbols TC_2.4
        fillTheForm(ConfProperties.getProperty("user11_name"), ConfProperties.getProperty("user11_password"),
                ConfProperties.getProperty("user11_password2"), ConfProperties.getProperty("user11_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.INVALID_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());
    }

    /**
     * TC_3 Cheching the "Password" textbox
     */
    @Test
    public void testPassword() {
        //Valid data for "Password" textbox TC_3.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Registration with password which length less than 8 TC_3.2
        fillTheForm(ConfProperties.getProperty("user12_name"), ConfProperties.getProperty("user12_password"),
                ConfProperties.getProperty("user12_password2"), ConfProperties.getProperty("user12_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_LENGTH.getMessage(), driver.findElement(By.xpath(passwordError)).getText());

        //Registration with password that doesn't contain at least one uppercase letter TC_3.3
        fillTheForm(ConfProperties.getProperty("user13_name"), ConfProperties.getProperty("user13_password"),
                ConfProperties.getProperty("user13_password2"), ConfProperties.getProperty("user13_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_UPPER_LETTER.getMessage(), driver.findElement(By.xpath(passwordError)).getText());

        //Registration with password that doesn't contain at least one non alphanumeric symbol TC_3.4
        fillTheForm(ConfProperties.getProperty("user14_name"), ConfProperties.getProperty("user14_password"),
                ConfProperties.getProperty("user14_password2"), ConfProperties.getProperty("user14_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_ALPHANUMERIC.getMessage(), driver.findElement(By.xpath(passwordError)).getText());

        //Registration with password that wasn't correctly confirmed TC_3.5
        fillTheForm(ConfProperties.getProperty("user16_name"), ConfProperties.getProperty("user16_password"),
                ConfProperties.getProperty("user16_password2"), ConfProperties.getProperty("user16_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_MATCH.getMessage(), driver.findElement(By.xpath(passwordError)).getText());
    }

    /**
     * TC_4 Checking the "Email" textbox
     */
    @Test
    public void testEmail() {
        //Valid data for "Email" textbox TC_4.1
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());

        //Registration with invalid email TC_4.2
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user15_password"),
                ConfProperties.getProperty("user15_password2"), ConfProperties.getProperty("user15_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.INVALID_EMAIL.getMessage(), driver.findElement(By.xpath(emailError)).getText());

        //Registration with already registered email TC_4.3
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), registeredEmail, Role.READ_WRITE.getRole());
        assertEquals(webFormURL, driver.getCurrentUrl()); // тест не проходит, т.к. функционал не соответствует документации
    }

    /**
     * TC_5 Checking the fields after incorrect input
     */
    @Test
    public void testFields() {
        //All fields remain filled after incorrect input
        fillTheForm(ConfProperties.getProperty("user11_name"), ConfProperties.getProperty("user11_password"),
                ConfProperties.getProperty("user11_password2"), ConfProperties.getProperty("user11_email"), Role.ADMIN.getRole());
        assertEquals(ConfProperties.getProperty("user11_name"), driver.findElement(By.id(loginField)).getAttribute("value"));
        assertEquals(ConfProperties.getProperty("user11_email"), driver.findElement(By.id(email)).getAttribute("value"));
        assertEquals(Role.ADMIN.getRoleAsAttribute(), driver.findElement(By.id(role)).getAttribute("value"));
    }

    /**
     * TC_6 Checking the availability of the form using the https protocol
     */
    @Test
    public void testURL() {
        driver.get(httpURL);
        assertEquals(webFormURL, driver.getCurrentUrl());
    }

    /**
     * TC_7
     */
    @Test
    public void testPasswordHiding() {
        //Checking each entered password character is hidden by stars
        driver.get(webFormURL);
        driver.findElement(By.id(loginField)).sendKeys("name123");
        driver.findElement(By.id(passwdField)).sendKeys("Password1+");
        driver.findElement(By.id(confirmPasswdField)).sendKeys("Password1+");
        driver.findElement(By.id(email)).sendKeys("mail@mail.com");
        driver.findElement(By.id(hidePassword)).click();
        boolean isChecked = driver.findElement(By.tagName("input")).isSelected(); //false
        assertEquals(isChecked, driver.findElement(By.id(passwdField)).isDisplayed());
    }

    /**
     * TC_10 Checking access to the system
     */
    @Test
    public void testAccessWithoutConfirmation() {
        //Checking access to the system by a user who has not confirmed registration
        String login = loginGenerator();
        String mail = mailGenerator();
        driver.get(webFormURL);
        fillTheForm(login, ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), mail, Role.ADMIN.getRole());
        driver.get(registeredURL);
        driver.findElement(By.name("j_username")).sendKeys(login);
        driver.findElement(By.name("j_password")).sendKeys(ConfProperties.getProperty("user2_password"));
        driver.findElement(By.name("submit")).click();
        assertEquals(registeredURL, driver.getCurrentUrl()); //пользователь получает доступ без email подтверждения
    }

    public String loginGenerator() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");
    }

    public String mailGenerator() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz") + "@" + "testmail.com";
    }

    @After
    public void tearDown() {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}