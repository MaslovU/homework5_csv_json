import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JSONArray listJson = new JSONArray();
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = null;

        try {
            list = parseCSV(columnMapping, fileName);
        } catch (NullPointerException e) {
            System.out.println("List is nuul");
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
        writeString(listJson);
    }

    private static void writeString(JSONArray listJson) {
        try (FileWriter fileWriter = new FileWriter("my_data.json")) {
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
}
