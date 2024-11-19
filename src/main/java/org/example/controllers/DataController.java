package org.example.controllers;

import org.example.model.Data;
import org.example.services.DataService;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.example.utils.DataHandler.createBuildData;

public class DataController {
    
    private static final DataService SERVICE = new DataService();
    
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(System.out));
    
    private static Map<Integer, Data> map = new HashMap<>();
    private static int                id  = SERVICE.loadIdByTxtFile();
    
    public void makeDirectory() {
        SERVICE.makeDirectory();
    }
    
    public void saveWiseSaying() throws IOException {
        String content;
        do {
            BW.write("명언: ");
            BW.flush();
            content = BR.readLine();
            stringEmptyCheck(content);
        } while (content.trim().isEmpty());
        
        String author;
        do {
            BW.write("작가: ");
            BW.flush();
            author = BR.readLine();
            stringEmptyCheck(author);
        } while (author.trim().isEmpty());
        
        Data data = Data.of(id, author, content);
        map.put(id, data);
        SERVICE.saveDataToJson(data);
        BW.write(id++ + "번 명언이 등록되었습니다.");
        BW.newLine();
        
        BW.flush();
    }
    
    public void printWiseSaying() throws IOException {
        BW.write("번호 / 작가 / 명언");
        BW.newLine();
        BW.write("----------------------");
        BW.newLine();
        
        int[] idArr = map.keySet().stream().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).toArray();
        for (int id : idArr) {
            BW.write(id + " / " + map.get(id).getAuthor() + " / " + map.get(id).getContent());
            BW.newLine();
        }
        BW.flush();
    }
    
    public void deleteWiseSaying(final int id) throws IOException {
        if (map.containsKey(id)) {
            SERVICE.deleteFile(id + ".json");
            map.remove(id);
            BW.write(id + "번 명언이 삭제되었습니다.");
            BW.newLine();
        } else {
            BW.write(id + "번 명언이 존재하지 않습니다.");
            BW.newLine();
        }
        BW.flush();
    }
    
    public void editWiseSaying(final int id) throws IOException {
        if (map.containsKey(id)) {
            Data data = map.get(id);
            
            BW.write("명언(기존): " + data.getContent());
            BW.newLine();
            
            String content;
            do {
                BW.write("명언: ");
                BW.flush();
                content = BR.readLine();
                stringEmptyCheck(content);
            } while (content.trim().isEmpty());
            
            BW.write("작가(기존): " + data.getAuthor());
            BW.newLine();
            
            String author;
            do {
                BW.write("작가: ");
                BW.flush();
                author = BR.readLine();
                stringEmptyCheck(author);
            } while (author.trim().isEmpty());
            
            SERVICE.deleteFile(data.getId() + ".json");
            data = Data.of(id, author, content);
            map.put(id, data);
            SERVICE.saveDataToJson(data);
        } else {
            BW.write(id + "번 명언은 존재하지 않습니다.");
            BW.newLine();
        }
        BW.flush();
    }
    
    public void buildWiseSaying() throws IOException {
        if (map.isEmpty()) {
            BW.write("데이터가 존재하지 않아 빌드할 수 없습니다.");
            BW.newLine();
            BW.flush();
            return;
        }
        
        SERVICE.saveStringToJson(createBuildData(map));
        
        BW.write("data.json 파일의 내용이 갱신되었습니다.");
        BW.newLine();
        BW.flush();
    }
    
    public void saveLastId() {
        SERVICE.saveIdToTxt(id);
    }
    
    public void loadAllData() {
        map = SERVICE.loadAllDataByStoreDir(map);
    }
    
    private void stringEmptyCheck(final String str) throws IOException {
        if (str.trim().isEmpty()) {
            BW.write("내용을 입력해주세요.");
            BW.newLine();
        }
    }
    
}
