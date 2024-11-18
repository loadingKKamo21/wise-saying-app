package org.example.util;

import org.example.model.Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Comparator;
import java.util.Map;

import static org.example.util.FileTool.*;

public class WiseSayingTool {
    
    public static int register(final BufferedWriter bw, final BufferedReader br, int idx, final Map<Integer, Data> map,
                               final String storeDir)
    throws IOException {
        String content;
        do {
            bw.write("명언: ");
            bw.flush();
            content = br.readLine();
            
            if (content.trim().isEmpty()) {
                bw.write("명언 내용을 입력해주세요.");
                bw.newLine();
            }
        } while (content.trim().isEmpty());
        
        String author;
        do {
            bw.write("작가: ");
            bw.flush();
            author = br.readLine();
            
            if (author.trim().isEmpty()) {
                bw.write("작가 이름을 입력해주세요.");
                bw.newLine();
            }
        } while (author.trim().isEmpty());
        
        Data data = Data.of(idx, author, content);
        map.put(idx, data);
        saveObjectToJson(idx, data, storeDir);
        bw.write(idx + "번 명언이 등록되었습니다.");
        bw.newLine();
        
        bw.flush();
        
        return ++idx;
    }
    
    public static void listPrint(final BufferedWriter bw, final Map<Integer, Data> map) throws IOException {
        bw.write("번호 / 작가 / 명언");
        bw.newLine();
        bw.write("----------------------");
        bw.newLine();
        
        int[] idxArr = map.keySet().stream().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).toArray();
        for (int idx : idxArr) {
            bw.write(idx + " / " + map.get(idx).getAuthor() + " / " + map.get(idx).getContent());
            bw.newLine();
        }
        
        bw.flush();
    }
    
    public static void delete(final BufferedWriter bw, final int idx, final Map<Integer, Data> map,
                              final String storeDir) throws IOException {
        if (map.containsKey(idx)) {
            deleteFile(storeDir + map.get(idx).getIdx() + ".json");
            map.remove(idx);
            bw.write(idx + "번 명언이 삭제되었습니다.");
            bw.newLine();
        } else {
            bw.write(idx + "번 명언은 존재하지 않습니다.");
            bw.newLine();
        }
    }
    
    public static void edit(final BufferedWriter bw, final BufferedReader br, final int idx,
                            final Map<Integer, Data> map, final String storeDir)
    throws IOException {
        if (map.containsKey(idx)) {
            Data data = map.get(idx);
            deleteFile(storeDir + data.getIdx() + ".json");
            bw.write("명언(기존): " + data.getContent());
            bw.newLine();
            
            String content;
            do {
                bw.write("명언: ");
                bw.flush();
                content = br.readLine();
                if (content.trim().isEmpty()) {
                    bw.write("명언 내용을 입력해주세요.");
                    bw.newLine();
                }
            } while (content.trim().isEmpty());
            
            bw.write("작가(기존): " + data.getAuthor());
            bw.newLine();
            String author;
            do {
                bw.write("작가: ");
                bw.flush();
                author = br.readLine();
                if (author.trim().isEmpty()) {
                    bw.write("작가 이름을 입력해주세요.");
                    bw.newLine();
                }
            } while (author.trim().isEmpty());
            
            saveObjectToJson(idx, data, storeDir);
            map.put(idx, Data.of(idx, author, content));
        } else {
            bw.write(idx + "번 명언은 존재하지 않습니다.");
            bw.newLine();
        }
    }
    
    public static void build(final BufferedWriter bw, final Map<Integer, Data> map, final String storeDir)
    throws IOException {
        int[] idxArr = map.keySet().stream().sorted().mapToInt(Integer::intValue).toArray();
        
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int idx : idxArr) {
            try (BufferedReader br = new BufferedReader(new StringReader(objectToJson(idx, map.get(idx))))) {
                String line;
                while ((line = br.readLine()) != null) sb.append("  ").append(line).append("\n");
            }
            sb.deleteCharAt(sb.lastIndexOf("\n"));
            sb.append(",\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        
        saveStringToJson(sb.toString(), storeDir);
        
        bw.write("data.json 파일의 내용이 갱신되었습니다.");
        bw.newLine();
    }
    
}
