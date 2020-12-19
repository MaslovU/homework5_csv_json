import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JSONArray listJson = new JSONArray();
        JSONArray listJson2 = new JSONArray();
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String jsonFromCsv = "my_data.json";
        String xmlFileName = "data.xml";
        String jsonFromXml = "my_data2.json";
        List<Employee> list = null;
        List<Employee> listFromXml = null;

        try {
            list = parseCSV(columnMapping, fileName);
        } catch (NullPointerException e) {
            System.out.println("List is null");
        }

        try {
            listFromXml = parseXML(xmlFileName);
        } catch (NullPointerException e) {
            System.out.println("List is null");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        for (Employee employee : list) {
            JSONObject object = new JSONObject();
            object.put("id", employee.id);
            object.put("firstName", employee.firstName);
            object.put("lastName", employee.lastName);
            object.put("country", employee.country);
            object.put("age", employee.age);
            listJson.add(object);
        }
        writeString(listJson, jsonFromCsv);

        for (Employee employee : listFromXml) {
            JSONObject object = new JSONObject();
            object.put("id", employee.id);
            object.put("firstName", employee.firstName);
            object.put("lastName", employee.lastName);
            object.put("country", employee.country);
            object.put("age", employee.age);
            listJson2.add(object);
        }
        writeString(listJson2, jsonFromXml);

        List<Employee> listFromJson = JsonToList.jsonToList(jsonFromCsv);
        for (Employee employee: listFromJson) {
            System.out.println(employee);
        }
    }

    private static void writeString(JSONArray listJson, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(listJson.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Employee> parseXML(String xmlFileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> employees = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlFileName));
        Node root = document.getDocumentElement();
//        System.out.println("Корневой элемент: " + root.getNodeName());
//        System.out.println();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
//                System.out.println("Текущий узел: " + node_.getNodeName());
//                System.out.println();
                Employee employee = new Employee();
                NodeList employeeProps = node_.getChildNodes();
                long searchId = Long.parseLong(employeeProps.item(1).getChildNodes().item(0).getTextContent());
                String searchFirstName = employeeProps.item(3).getChildNodes().item(0).getTextContent();
                String searchLastName = employeeProps.item(5).getChildNodes().item(0).getTextContent();
                String searchCountry = employeeProps.item(7).getChildNodes().item(0).getTextContent();
                int searchAge = Integer.parseInt(employeeProps.item(9).getChildNodes().item(0).getTextContent());
                employee.id = searchId;
                employee.firstName = searchFirstName;
                employee.lastName = searchLastName;
                employee.country = searchCountry;
                employee.age = searchAge;
                employees.add(employee);
            }
        }
        return employees;
    }
}
