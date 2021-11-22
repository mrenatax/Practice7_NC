package pr8;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RestTest {
    public static final String invalidServiceName = "Invalid serviceName";
    public static final String validServiceNameXML = "http://www.mocky.io/v2/5bebe91f3300008500fbc0e3";
    public static final String validServiceNameJson = "http://www.mocky.io/v2/5bed52fd3300004c00a2959d";

    @Test
    public void testHeader() throws IOException {
        Rest rest = new Rest();
        assertEquals("TA-Fall-2018", rest.getHeader());
    }

    @Test
    public void testWriter() throws IOException {
        Rest rest = new Rest();
        rest.writer(invalidServiceName, "outputFiles\\testWriter\\newFile.xml");
        rest.writer(validServiceNameXML, "outputFiles\\testWriter\\newFile.xml");
        rest.writer(validServiceNameJson, "outputFiles\\testWriter\\newFile.json");
    }

    @Test
    public void testParseXML() throws IOException {
        Rest rest = new Rest();
        rest.parseXML("outputFiles\\testParseXML\\test.xml");
    }

    @Test
    public void testParseJson() throws IOException {
        Rest rest = new Rest();
        rest.parseJson("outputFiles\\testParseJson\\test.json");
    }

}
