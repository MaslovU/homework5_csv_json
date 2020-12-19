import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JsonToList {

    public static String readString(String filePath) {
        JSONParser jsonParser = new JSONParser();
        String string = null;

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = jsonParser.parse(reader);
            JSONArray employeeList = (JSONArray) obj;
            string = employeeList.toJSONString();
//            System.out.println(employeeList);
//            System.out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static List<Employee> jsonToList(String file) {
        String json = readString(file);
        Gson gson = new Gson();
        List<Employee> list = gson.fromJson(json, new TypeToken<List<Employee>>() {
        }.getType());
        return list;
    }
}
