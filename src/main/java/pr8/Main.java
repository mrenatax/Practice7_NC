package pr8;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Rest rest = new Rest();
        System.out.println("Значение нестандартного http header'а X-Ta-Course-Example-Header:\n" +
                rest.getHeader() + "\n");
        rest.parseXML("outputFiles\\Main\\file.xml");
        rest.parseJson("outputFiles\\Main\\file.json");
    }
}
