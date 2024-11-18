package org.example;

import org.example.model.Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.example.util.FileTool.*;
import static org.example.util.WiseSayingTool.*;

public class Main {
    
    private static final String STORE_DIR = "./db/wiseSaying/";
    
    private static Map<Integer, Data> map = new HashMap<>();
    private static int                IDX;
    
    public static void main(String[] args) throws IOException {
        makeDirectory(STORE_DIR);
        IDX = loadLastId(STORE_DIR);
        loadAllJsonFiles(map, STORE_DIR);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        
        bw.write("== 명언 앱 ==");
        bw.newLine();
        bw.flush();
        String command;
        do {
            bw.write("명령) ");
            bw.flush();
            command = br.readLine();
            
            if ("등록".equals(command))
                IDX = register(bw, br, IDX, map, STORE_DIR);
            else if ("목록".equals(command))
                listPrint(bw, map);
            else if (command.startsWith("삭제?id="))
                delete(bw, Integer.parseInt(command.substring(command.indexOf("=") + 1)), map, STORE_DIR);
            else if (command.startsWith("수정?id="))
                edit(bw, br, Integer.parseInt(command.substring(command.indexOf("=") + 1)), map, STORE_DIR);
            else if ("빌드".equals(command))
                build(bw, map, STORE_DIR);
            else if ("종료".equals(command))
                break;
            else {
                bw.write("'" + command + "'는(은) 올바르지 않은 명령입니다.");
                bw.newLine();
            }
            
            bw.flush();
        } while (true);
        
        bw.close();
        br.close();
        
        saveLastIdTxt(IDX, STORE_DIR);
    }
    
}