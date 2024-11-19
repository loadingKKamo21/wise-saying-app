package org.example.repositories;

import org.example.model.Data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import static org.example.utils.DataHandler.dataToJson;
import static org.example.utils.DataHandler.jsonToData;

public class DataRepository {
    
    private static final String JSON_EXT_SUFFIX = ".json";
    private static final String TXT_EXT_SUFFIX  = ".txt";
    private static final String DATA            = "data";
    private static final String LAST_ID         = "lastId";
    
    public void makeDirectory(final String storeDir) {
        File dir = new File(storeDir);
        if (!dir.exists()) dir.mkdirs();
    }
    
    public void saveDataToJson(final Data data, final String storeDir) throws IOException {
        String json = dataToJson(data);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storeDir + "/" + data.getId() + JSON_EXT_SUFFIX))) {
            bw.write(json);
        }
    }
    
    public void saveStringToJson(final String str, final String storeDir) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storeDir + "/" + DATA + JSON_EXT_SUFFIX))) {
            bw.write(str);
        }
    }
    
    public void saveIdToTxt(final int id, final String storeDir) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storeDir + "/" + LAST_ID + TXT_EXT_SUFFIX))) {
            bw.write(String.valueOf(id));
        }
    }
    
    public Data loadDataByJsonFile(final String filePath) throws IOException {
        if (!new File(filePath).exists()) return null;
        
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }
        
        return jsonToData(sb.toString());
    }
    
    public Map<Integer, Data> loadAllDataByStoreDir(final Map<Integer, Data> map, final String storeDir)
    throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(storeDir))) {
            paths.filter(Files::isRegularFile).filter(path -> !path.toString().endsWith(DATA + JSON_EXT_SUFFIX))
                 .filter(path -> path.toString().endsWith(DATA)).forEach(path -> {
                     try {
                         Data data = loadDataByJsonFile(path.toString());
                         map.put(data.getId(), data);
                     } catch (IOException e) {
                         System.err.println("Error reading file: " + path);
                         e.printStackTrace();
                     }
                 });
        }
        return map;
    }
    
    public int loadIdByTxtFile(final String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath + "/" + LAST_ID + TXT_EXT_SUFFIX))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException e) {
//            e.printStackTrace();
            return 1;
        }
    }
    
    public void deleteFile(final String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.delete(path);
    }
    
}
