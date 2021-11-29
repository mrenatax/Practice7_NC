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
    private static final String jsonpathNamePath = "$.breakfast_menu.food[*].name";
    private static final String filterCaloriesPath = "$.breakfast_menu.food[?(@.calories < 700)].price";
    private static final String maxCaloriePath = "$.max($.breakfast_menu.food[*].calories)";
    private static final String filterNumbersPath = "$.max($.breakfast_menu.numbers[*])";

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
    public int writer(String serviceName, String filename) throws IOException {
        try {
            HttpResponse httpResponse = httpGet(serviceName);
            Scanner sc = new Scanner(httpResponse.getEntity().getContent());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            while (sc.hasNext()) {
                writer.write(sc.nextLine());
            }
            writer.close();
            return 0;
        } catch (NullPointerException e) {
            System.err.println("Service must be a valid url");
            return -1;
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
     */
    public NodeList parseXML(String fileName) {
        try {
            writer(xmlService, fileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName("food");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFoodListXML(NodeList list) {
        StringBuilder listBuilder = new StringBuilder("(XML) Список блюд:\n");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("name").item(0).getTextContent(); // get name
                listBuilder.append("- ").append(name).append("\n");
            }
        }
        return listBuilder.toString();
    }

    public String getFoodPriceLess700CaloriesXML(NodeList list) {
        StringBuilder priceBuilder = new StringBuilder("Цены блюд с калорийностью < 700:\n");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList salaryNodeList = element.getElementsByTagName("calories");
                String calories = salaryNodeList.item(0).getTextContent();
                if (Integer.parseInt(calories) < 700) {
                    priceBuilder.append(element.getElementsByTagName("price").item(0).getTextContent()).append("\n");
                }
            }
        }
        return priceBuilder.toString();
    }

    public String getMaxCalorieDishXML(NodeList list) {
        int k = 0;
        String nameOfMostCalorie = "";
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList salaryNodeList = element.getElementsByTagName("calories");
                String calories = salaryNodeList.item(0).getTextContent();
                if (Integer.parseInt(calories) > k) {
                    k = Integer.parseInt(calories);
                    nameOfMostCalorie = element.getElementsByTagName("name").item(0).getTextContent();
                }
            }
        }
        return nameOfMostCalorie;
    }

    public DocumentContext parseJson(String fileName) {
        try {
            writer(jsonService, fileName);
            return JsonPath.parse(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFoodListJson(DocumentContext jsonContext) {
        return "\n(JSON) Список блюд:\n" + jsonContext.read(jsonpathNamePath).toString();
    }

    public String getFoodPriceLess700CaloriesJson(DocumentContext jsonContext) {
        return ("\nЦены блюд с калорийностью < 700:\n" + jsonContext.read(filterCaloriesPath).toString());
    }

    public String getMaxNumberJson(DocumentContext jsonContext) {
        return ("\nМаксимальное число из массива numbers: \n" + jsonContext.read(filterNumbersPath).toString());
    }

    public String getMaxCalorieDishJson(DocumentContext jsonContext) {
        String maxCalorieString = jsonContext.read(maxCaloriePath).toString();
        double maxCalorie = Double.parseDouble(maxCalorieString);
        String nameOfMaxCaloriePath = "$.breakfast_menu.food.[?(@.calories == " + maxCalorie + " )].name";
        return jsonContext.read(nameOfMaxCaloriePath).toString();
    }
}
