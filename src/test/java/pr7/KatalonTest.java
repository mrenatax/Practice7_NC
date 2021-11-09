package pr7;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class KatalonTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testUntitledTestCase() {
        driver.get("https://www.google.com/");
        driver.findElement(By.name("q")).clear();
        driver.get("https://www.netcracker.com/");
        driver.findElement(By.linkText("Careers")).click();
        driver.findElement(By.xpath("//a[@id='search-jobs']/span")).click();
        driver.findElement(By.xpath("//div[@id='jobdept-group']/div/button/span")).click();
        driver.findElement(By.xpath("//div[@id='jobdept-group']/div/div/ul/li[14]/a/span")).click();
        new Select(driver.findElement(By.id("jobdept"))).selectByVisibleText("Quality Assurance");
        driver.findElement(By.xpath("//div[@id='location-group']/div/button/span")).click();
        driver.findElement(By.xpath("//div[@id='location-group']/div/div/ul/li[12]/a/span")).click();
        new Select(driver.findElement(By.id("location"))).selectByVisibleText("Netherlands");
        driver.findElement(By.xpath("//form[@id='positionslist-filterform']/fieldset/div/label")).click();
    }

    @After
    public void tearDown() {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}