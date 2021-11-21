package pr8;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Rest rest = new Rest();
        rest.getHeader();
        rest.parseXML("test.xml");
        rest.parseJson("test.json");
    }
}
