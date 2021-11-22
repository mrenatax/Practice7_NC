package pr8;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Rest {
    private static final String xmlService = "http://www.mocky.io/v2/5bebe91f3300008500fbc0e3";
    private static final String jsonService = "http://www.mocky.io/v2/5bed52fd3300004c00a2959d";

    /**
     * Method for http client  work
     *
     * @param serviceName xml/json service
     * @return http response
     * @throws IOException exception
     */
    public HttpResponse httpGet(String serviceName) throws IOException {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault(); //Creating a HttpClient object
            HttpGet httpGet = new HttpGet(serviceName); //Creating a HttpGet object
            return httpclient.execute(httpGet);
        } catch (ClientProtocolException | IllegalArgumentException e) {
            System.err.println("\nInvalid client protocol:");
            return null;
        }
    }

    /**
     * Method for writing data to xml/json file
     *
     * @param serviceName xml/json service
     * @param filename    xml/json file
     * @throws IOException exception
     */
    public void writer(String serviceName, String filename) throws IOException {
        try {
            HttpResponse httpResponse = httpGet(serviceName);
            Scanner sc = new Scanner(httpResponse.getEntity().getContent());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            while (sc.hasNext()) {
                writer.write(sc.nextLine());
            }
            writer.close();
        } catch (NullPointerException e) {
            System.err.println("Service must be a valid url");
        }
    }

    /**
     * Method for searching http header (task 3)
     *
     * @throws IOException exception
     */
    public String getHeader() throws IOException {
        Header[] headers = httpGet(xmlService).getAllHeaders();
        for (Header header : headers) {
            if (header.getName().equals("X-TA-Course-Example-Header")) {
                return (header.getValue());
            }
        }
        return "Header not found";
    }

    /**
     * XML parser
     *
     * @param fileName xml file
     * @throws IOException exception
     */
    public void parseXML(String fileName) throws IOException {
        writer(xmlService, fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("food");
            StringBuilder listBuilder = new StringBuilder("(XML) Список блюд:\n");
            StringBuilder priceBuilder = new StringBuilder("Цены блюд с калорийностью < 700:\n");
            int k = 0;
            String nameOfMostCalorie = "";
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent(); // get name
                    listBuilder.append("- ").append(name).append("\n");

                    NodeList salaryNodeList = element.getElementsByTagName("calories");
                    String calories = salaryNodeList.item(0).getTextContent();
                    if (Integer.parseInt(calories) < 700) {
                        priceBuilder.append(element.getElementsByTagName("price").item(0).getTextContent()).append("\n");
                    }
                    if (Integer.parseInt(calories) > k) {
                        k = Integer.parseInt(calories);
                        nameOfMostCalorie = element.getElementsByTagName("name").item(0).getTextContent();
                    }
                }
            }
            System.out.println(listBuilder + "\n" + priceBuilder +
                    "\n" + "Название самого калорийного блюда: " + nameOfMostCalorie);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public void parseJson(String fileName) throws IOException {
        writer(jsonService, fileName);

        String jsonpathNamePath = "$.breakfast_menu.food[*].name";
        DocumentContext jsonContext = JsonPath.parse(new File(fileName));
        String jsonpathName = jsonContext.read(jsonpathNamePath).toString();
        System.out.println("\n(JSON) Список блюд:\n" + jsonpathName);

        String filterCaloriesPath = "$.breakfast_menu.food[?(@.calories < 700)].price";
        String filterCalories = jsonContext.read(filterCaloriesPath).toString();
        System.out.println("\nЦены блюд с калорийностью < 700:\n" + filterCalories);

        String maxCaloriePath = "$.max($.breakfast_menu.food[*].calories)";
        String maxCalorieString = jsonContext.read(maxCaloriePath).toString();
        double maxCalorie = Double.parseDouble(maxCalorieString);
        String nameOfMaxCaloriePath = "$.breakfast_menu.food.[?(@.calories == " + maxCalorie + " )].name";
        String nameOfMaxCalorie = jsonContext.read(nameOfMaxCaloriePath).toString();
        System.out.println("\n Назавние самого калорийного блюда:\n" + nameOfMaxCalorie);

        String filterNumbersPath = "$.max($.breakfast_menu.numbers[*])";
        String filterNumbers = jsonContext.read(filterNumbersPath).toString();
        System.out.println("\n Максимальное число из массива numbers: \n" + filterNumbers);
    }
}
