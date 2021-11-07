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
        //options.addArguments("--headless"); // полезно для ускорения тестов - не запускать визуальную часть браузера.
        options.addArguments("--no-sandbox"); // без этого хромне заведется
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        System.setProperty("webdriver.chrome.driver", "");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //driver.get(webFormURL);
    }

    /**
     * метод для заполнения полей
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
     * TC_1.1
     */
    @Test
    public void testAdminRoleRegistration() {
        //Admin role
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), mailGenerator(), Role.ADMIN.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_1.2
     */
    @Test
    public void testReadOnlyRoleRegistration() {
        //Read-only role
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user3_password"),
                ConfProperties.getProperty("user3_password2"), mailGenerator(), Role.READ_ONLY.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_1.3
     */
    @Test
    public void testReadWriteRoleRegistration() {
        //Read-write role
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user4_password"),
                ConfProperties.getProperty("user4_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_2.1
     */
    @Test
    public void testUsernameValid() {
        //valid username
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_2.2
     */
    @Test
    public void testUsernameRegistered() {
        //Registration with username which was already registred
        fillTheForm(registeredLogin, ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.REGISTERED_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());
    }

    /**
     * TC_2.3
     */
    @Test
    public void testUsernameLength() {
        //Registration with username which length less than 6
        fillTheForm(ConfProperties.getProperty("user1_name"), ConfProperties.getProperty("user1_password"),
                ConfProperties.getProperty("user1_password2"), ConfProperties.getProperty("user1_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.INVALID_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());

    }

    /**
     * TC_2.4
     */
    @Test
    public void testUsernameAlphanumeric() {
        //Registration with username which contains non alphanumeric symbols
        fillTheForm(ConfProperties.getProperty("user11_name"), ConfProperties.getProperty("user11_password"),
                ConfProperties.getProperty("user11_password2"), ConfProperties.getProperty("user11_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.INVALID_LOGIN.getMessage(), driver.findElement(By.xpath(loginError)).getText());
    }

    /**
     * TC_3.1
     */
    @Test
    public void testValidPassword() {
        //Valid data for "Password" textbox
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_3.2
     */
    @Test
    public void testPasswordLength() {
        //Registration with password which length less than 8
        fillTheForm(ConfProperties.getProperty("user12_name"), ConfProperties.getProperty("user12_password"),
                ConfProperties.getProperty("user12_password2"), ConfProperties.getProperty("user12_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_LENGTH.getMessage(), driver.findElement(By.xpath(passwordError)).getText());
    }

    /**
     * TC_3.3
     */
    @Test
    public void testPasswordUppercaseLetter() {
        //Registration with password that doesn't contain at least one uppercase letter
        fillTheForm(ConfProperties.getProperty("user13_name"), ConfProperties.getProperty("user13_password"),
                ConfProperties.getProperty("user13_password2"), ConfProperties.getProperty("user13_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_UPPER_LETTER.getMessage(), driver.findElement(By.xpath(passwordError)).getText());
    }

    /**
     * TC_3.4
     */
    @Test
    public void testPasswordAlphanumeric() {
        //Registration with password that doesn't contain at least one non alphanumeric symbol
        fillTheForm(ConfProperties.getProperty("user14_name"), ConfProperties.getProperty("user14_password"),
                ConfProperties.getProperty("user14_password2"), ConfProperties.getProperty("user14_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_ALPHANUMERIC.getMessage(), driver.findElement(By.xpath(passwordError)).getText());
    }

    /**
     * TC_3.5
     */
    @Test
    public void testPasswordConfirmed() {
        //Registration with password that wasn't correctly confirmed
        fillTheForm(ConfProperties.getProperty("user16_name"), ConfProperties.getProperty("user16_password"),
                ConfProperties.getProperty("user16_password2"), ConfProperties.getProperty("user16_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.PASSWORD_MATCH.getMessage(), driver.findElement(By.xpath(passwordError)).getText());
    }

    /**
     * TC_4.1
     */
    @Test
    public void testValidEmail() {
        //Valid data for "Email" textbox
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user10_password"),
                ConfProperties.getProperty("user10_password2"), mailGenerator(), Role.READ_WRITE.getRole());
        assertEquals(registeredURL, driver.getCurrentUrl());
    }

    /**
     * TC_4.2
     */
    @Test
    public void testInvalidEmail() {
        //Registration with invalid email
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user15_password"),
                ConfProperties.getProperty("user15_password2"), ConfProperties.getProperty("user15_email"), Role.READ_WRITE.getRole());
        assertEquals(ErrorMessage.INVALID_EMAIL.getMessage(), driver.findElement(By.xpath(emailError)).getText());
    }

    /**
     * TC_4.3
     */
    @Test
    public void testRegisteredEmail() {
        //Registration with already registered email
        fillTheForm(loginGenerator(), ConfProperties.getProperty("user2_password"),
                ConfProperties.getProperty("user2_password2"), registeredEmail, Role.READ_WRITE.getRole());
        /*if(registeredURL.equals(driver.getCurrentUrl())){
            System.err.println("Registration with already registered email was successful");
        }*/
        assertEquals(ErrorMessage.REGISTERED_EMAIL.getMessage(), driver.findElement(By.xpath(emailError)).getText());
    }

    /**
     * TC_5.1
     */
    @Test
    public void testFields() {
        //All fields remain filled after incorrect input
        fillTheForm(ConfProperties.getProperty("user11_name"), ConfProperties.getProperty("user11_password"),
                ConfProperties.getProperty("user11_password2"), ConfProperties.getProperty("user11_email"), Role.ADMIN.getRole());
        assertEquals(ConfProperties.getProperty("user11_name"), driver.findElement(By.id(loginField)).getAttribute("value"));
        //assertEquals(ConfProperties.getProperty("user11_password"), driver.findElement(By.id(passwdField)).getAttribute("value"));
        //assertEquals(ConfProperties.getProperty("user11_password2"), driver.findElement(By.id(confirmPasswdField)).getAttribute("value"));
        assertEquals(ConfProperties.getProperty("user11_email"), driver.findElement(By.id(email)).getAttribute("value"));
        assertEquals(Role.ADMIN.getRoleAsAttribute(), driver.findElement(By.id(role)).getAttribute("value"));
    }

    /**
     * TC_6
     */
    @Test
    public void testURL() {
        //Checking the availability of the form using the https protocol
        driver.get(httpURL);
        assertEquals(webFormURL, driver.getCurrentUrl());
    }

    /**
     * TC_7.1 - 8.1
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
        boolean isChecked = driver.findElement(By.tagName("input")).isSelected();
        assertEquals(isChecked, driver.findElement(By.id(passwdField)).isDisplayed());
    }

    /**
     * TC_9.1-10.1
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
        //assertEquals(driver.getCurrentUrl(), "https://inventory.edu-netcracker.com/pages/startpage.xhtml");
        assertEquals(driver.getCurrentUrl(), registeredURL);

    }

    public String loginGenerator() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");
    }

    public String mailGenerator() {
        return RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz") + "@" + "testmail.com";
    }

    @After
    public void tearDown() {
        // driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}