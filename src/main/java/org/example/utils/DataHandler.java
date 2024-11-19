package org.example.utils;

import org.example.model.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class DataHandler {
    
    public static String dataToJson(final Data data) {
        return "{\n" +
               "  \"id\": " + data.getId() + ",\n" +
               "  \"author\": \"" + data.getAuthor() + "\",\n" +
               "  \"content\": \"" + data.getContent() + "\"\n" +
               "}";
    }
    
    public static Data jsonToData(final String json) {
        int    id      = Integer.parseInt(json.split("\"id\":")[1].split(",")[0].trim());
        String author  = json.split("\"author\":")[1].split(",")[0].replace("\"", "").trim();
        String content = json.split("\"content\":")[1].split(",")[0].replaceAll("\"|}|]", "").trim();
        
        return Data.of(id, author, content);
    }
    
    public static String createBuildData(final Map<Integer, Data> map) throws IOException {
        int[] idArr = map.keySet().stream().sorted().mapToInt(Integer::intValue).toArray();
        
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int id : idArr) {
            try (BufferedReader br = new BufferedReader(new StringReader(dataToJson(map.get(id))))) {
                String line;
                while ((line = br.readLine()) != null) sb.append("  ").append(line).append("\n");
            }
            sb.deleteCharAt(sb.lastIndexOf("\n"));
            sb.append(",\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        
        return sb.toString();
    }
    
}
