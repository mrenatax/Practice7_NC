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
        assertEquals(-1, rest.writer(invalidServiceName, "outputFiles\\testWriter\\newFile.xml"));
        assertEquals(0, rest.writer(validServiceNameXML, "outputFiles\\testWriter\\newFile.xml"));
        assertEquals(0, rest.writer(validServiceNameJson, "outputFiles\\testWriter\\newFile.json"));
    }

    @Test
    public void testParser() {
        Rest rest = new Rest();
        assertNotNull((rest.parseXML("outputFiles\\testParseXML\\test.xml")));
        assertNotNull(rest.parseJson("outputFiles\\testParseJson\\test.json"));
    }

    @Test
    public void testHttpGet() throws IOException {
        Rest rest = new Rest();
        assertNotNull(rest.httpGet(validServiceNameJson));
        assertNull(rest.httpGet(invalidServiceName));
    }

    @Test
    public void testGetFoodList() {
        Rest rest = new Rest();
        assertEquals("(XML) Список блюд:\n" +
                "- Belgian Waffles\n" +
                "- Strawberry Belgian Waffles\n" +
                "- Berry-Berry Belgian Waffles\n" +
                "- French Toast\n" +
                "- Homestyle Breakfast\n", rest.getFoodListXML(rest.parseXML("outputFiles\\testParseXML\\test.xml")));
        assertEquals("\n(JSON) Список блюд:\n" +
                        "[\"Belgian Waffles\",\"Strawberry Belgian Waffles\",\"Berry-Berry Belgian Waffles\",\"French Toast\",\"Homestyle Breakfast\"]",
                rest.getFoodListJson(rest.parseJson("outputFiles\\testParseJson\\test.json")));
    }

    @Test
    public void testGetFoodPriceLess700CaloriesXML() {
        Rest rest = new Rest();
        assertEquals("Цены блюд с калорийностью < 700:\n" +
                "$5.95\n" +
                "$4.50\n", rest.getFoodPriceLess700CaloriesXML(rest.parseXML("outputFiles\\testParseXML\\test.xml")));
        assertEquals("\nЦены блюд с калорийностью < 700:\n" +
                "[\"$5.95\",\"$4.50\"]", rest.getFoodPriceLess700CaloriesJson(rest.parseJson("outputFiles\\testParseJson\\file.json")));
    }

    @Test
    public void testGetMaxCalorieDish() {
        Rest rest = new Rest();
        assertEquals("Homestyle Breakfast", rest.getMaxCalorieDishXML(rest.parseXML("outputFiles\\testParseXML\\test.xml")));
        assertEquals("[\"Homestyle Breakfast\"]", rest.getMaxCalorieDishJson(rest.parseJson("outputFiles\\testParseJson\\file.json")));
    }

    @Test
    public void testGetMaxNumberJson() {
        Rest rest = new Rest();
        assertEquals("\nМаксимальное число из массива numbers: \n" +
                "950.0", rest.getMaxNumberJson(rest.parseJson("outputFiles\\testParseJson\\file.json")));
    }
}
