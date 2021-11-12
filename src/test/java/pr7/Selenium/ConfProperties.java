package pr7.Selenium;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfProperties {
    protected static Properties PROPERTIES;

    static {
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/conf.properties")) {
            PROPERTIES = new Properties();
            PROPERTIES.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for returning a string with a value from a file with parameters
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}