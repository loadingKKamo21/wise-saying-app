package org.example.util;

import org.example.model.Data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class FileTool {
    
    public static void makeDirectory(final String fileDir) {
        File file = new File(fileDir);
        if (!file.exists()) file.mkdirs();
    }
    
    public static String objectToJson(final int idx, final Data data) {
        return "{\n" +
               "  \"idx\": " + idx + ",\n" +
               "  \"author\": \"" + data.getAuthor() + "\",\n" +
               "  \"content\": \"" + data.getContent() + "\"\n" +
               "}";
    }
    
    public static void saveObjectToJson(final int idx, final Data data, final String fileDir) throws IOException {
        String json = objectToJson(idx, data);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileDir + idx + ".json"))) {
            bw.write(json);
        }
    }
    
    public static void saveStringToJson(final String str, final String fileDir) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileDir + "data.json"))) {
            bw.write(str);
        }
    }
    
    public static Data loadJsonToObject(final String filePath) throws IOException {
        if (!new File(filePath).exists()) return null;
        
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }
        
        String json    = sb.toString();
        int    idx     = Integer.parseInt(json.split("\"idx\":")[1].split(",")[0].trim());
        String author  = json.split("\"author\":")[1].split(",")[0].replace("\"", "").trim();
        String content = json.split("\"content\":")[1].split(",")[0].replaceAll("\"|}|]", "").trim();
        
        return Data.of(idx, author, content);
    }
    
    public static void loadAllJsonFiles(final Map<Integer, Data> map, final String dirPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dirPath))) {
            paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".json")).forEach(path -> {
                try {
                    Data data = loadJsonToObject(path.toString());
                    map.put(data.getIdx(), data);
                } catch (IOException e) {
                    System.err.println("Error reading file: " + path);
                    e.printStackTrace();
                }
            });
        }
    }
    
    public static int loadLastId(final String fileDir) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileDir + "lastId.txt"))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException e) {
            return 1;
        }
    }
    
    public static void saveLastIdTxt(final int idx, final String fileDir) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileDir + "lastId.txt"))) {
            bw.write(String.valueOf(idx));
        }
    }
    
    public static void deleteFile(final String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.delete(path);
    }
    
}
