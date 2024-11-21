package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import org.example.entities.WiseSaying;

public class WiseSayingHandler {

    public static String toJson(final WiseSaying param) {
        return "{\n" +
                "  \"id\": " + param.getId() + ",\n" +
                "  \"author\": \"" + param.getAuthor() + "\",\n" +
                "  \"content\": \"" + param.getContent() + "\"\n" +
                "}";
    }

    public static WiseSaying toEntity(final String json) {
        int id = Integer.parseInt(json.split("\"id\":")[1].split(",")[0].trim());
        String author = json.split("\"author\":")[1].split(",")[0].replace("\"", "").trim();
        String content = json.split("\"content\":")[1].split(",")[0].replaceAll("\"|}|]", "").trim();

        return WiseSaying.of(id, author, content);
    }

    public static String buildData(final Map<Integer, WiseSaying> map) throws IOException {
        int[] idArr = map.keySet().stream().sorted().mapToInt(Integer::intValue).toArray();

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int id : idArr) {
            try (BufferedReader br = new BufferedReader(new StringReader(toJson(map.get(id))))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append("  ").append(line).append("\n");
                }
            }
            sb.deleteCharAt(sb.lastIndexOf("\n"));
            sb.append(",\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");

        return sb.toString();
    }

}
