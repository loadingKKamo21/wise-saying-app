package org.example.repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;
import org.example.entities.WiseSaying;
import org.example.error.ex.ErrorCode;
import org.example.error.ex.GlobalException;
import org.example.utils.WiseSayingHandler;

public class WiseSayingRepositoryImpl implements WiseSayingRepository {

    private static final String STORE_DIR = "./db/wiseSaying";
    private static final String DATA = "data";
    private static final String LAST_ID = "lastId";

    @Override
    public void makeDirectory() {
        File dir = new File(STORE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void saveToJson(final WiseSaying param) throws IOException {
        String json = WiseSayingHandler.toJson(param);
        String filePath = STORE_DIR + File.separator + param.getId() + Ext.JSON;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(json);
        }
    }

    @Override
    public void buildData(final String param) throws IOException {
        String filePath = STORE_DIR + File.separator + DATA + Ext.JSON;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(param);
        }
    }

    @Override
    public WiseSaying loadFromJson(final String filePath) throws IOException {
        if (!new File(filePath).exists()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        return WiseSayingHandler.toEntity(sb.toString());
    }

    @Override
    public Map<Integer, WiseSaying> loadAll(final Map<Integer, WiseSaying> map) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(STORE_DIR))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> !path.toString().endsWith(DATA + Ext.JSON))
                    .filter(path -> path.toString().endsWith(Ext.JSON))
                    .forEach(path -> {
                        try {
                            WiseSaying wiseSaying = loadFromJson(path.toString());
                            map.put(wiseSaying.getId(), wiseSaying);
                        } catch (IOException e) {
                            throw new GlobalException(ErrorCode.FILE_LOAD_FAILED);
                        }
                    });
        }
        return map;
    }

    @Override
    public void saveLastIdTxt(final int id) throws IOException {
        String filePath = STORE_DIR + File.separator + LAST_ID + Ext.TXT;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(String.valueOf(id));
        }
    }

    @Override
    public boolean hasLastId() {
        String filePath = STORE_DIR + File.separator + LAST_ID + Ext.TXT;
        return new File(filePath).exists();
    }

    @Override
    public int loadLastId() throws IOException {
        String filePath = STORE_DIR + File.separator + LAST_ID + Ext.TXT;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return Integer.parseInt(br.readLine());
        }
    }

    @Override
    public void deleteJson(final int id) throws IOException {
        Path path = Paths.get(STORE_DIR + File.separator + id + Ext.JSON);
        Files.delete(path);
    }

}
