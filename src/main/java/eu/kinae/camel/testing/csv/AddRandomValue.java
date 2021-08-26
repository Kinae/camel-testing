package eu.kinae.camel.testing.csv;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class AddRandomValue {

    @Handler
    public List<Map> process()  {
        List<Map> list = new LinkedList<>();

        String[] array = new String[] {"A", "B", "C"};
        for (String s : array) {
            Map<String, Object> map = new LinkedHashMap<>();
            updateMap(map, "contract_reference", s);
            updateMap(map, "phone_number", s);
            list.add(map);
        }

        return list;
    }

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HHmmss");

    protected void updateMap(Map<String, Object> map, String column, String value) {
        map.put(column, value);
    }

}
