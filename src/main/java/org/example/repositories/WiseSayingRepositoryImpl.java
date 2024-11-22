package org.example.repositories;

import static org.example.error.ex.ErrorCode.INVALID_ID;
import static org.example.repositories.Ext.JSON;
import static org.example.repositories.Ext.TXT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;
import org.example.error.ex.ErrorCode;
import org.example.error.ex.GlobalException;
import org.example.utils.WiseSayingHandler;

public class WiseSayingRepositoryImpl implements WiseSayingRepository {

    private static final String DATA = "data";
    private static final String LAST_ID = "lastId";

    private final Map<Integer, WiseSaying> map;
    private final String storeDir;
    private int currentId;

    public WiseSayingRepositoryImpl(final String storeDir) {
        this.storeDir = storeDir;
        this.map = new LinkedHashMap<>();
        this.currentId = loadLastId();
    }

    @Override
    public void makeDirectory() {
        File dir = new File(storeDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public WiseSaying saveToJson(final WiseSayingReq param) throws IOException {
        WiseSaying wiseSaying = WiseSaying.of(currentId, param.getAuthor(), param.getContent());
        map.put(currentId++, wiseSaying);

        String json = WiseSayingHandler.toJson(wiseSaying);
        String filePath = storeDir + File.separator + wiseSaying.getId() + JSON;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(json);
        }

        return wiseSaying;
    }

    @Override
    public WiseSaying update(final int id, final WiseSayingReq param) throws IOException {
        if (id < 0 || !map.containsKey(id)) {
            throw new GlobalException(INVALID_ID);
        }

        WiseSaying wiseSaying = WiseSaying.of(id, param.getAuthor(), param.getContent());
        map.put(id, wiseSaying);

        String json = WiseSayingHandler.toJson(wiseSaying);
        String filePath = storeDir + File.separator + wiseSaying.getId() + JSON;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(json);
        }

        return wiseSaying;
    }

    @Override
    public void buildData(final String param) throws IOException {
        String filePath = storeDir + File.separator + DATA + JSON;
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
    public Map<Integer, WiseSaying> loadAll() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(storeDir))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> !path.toString().endsWith(DATA + JSON))
                    .filter(path -> path.toString().endsWith(JSON))
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
    public void saveLastIdTxt() throws IOException {
        String filePath = storeDir + File.separator + LAST_ID + TXT;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(String.valueOf(currentId));
        }
    }

    @Override
    public boolean hasLastId() {
        String filePath = storeDir + File.separator + LAST_ID + TXT;
        return new File(filePath).exists();
    }

    @Override
    public int loadLastId() {
        String filePath = storeDir + File.separator + LAST_ID + TXT;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException e) {
//            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public void deleteJson(final int id) throws IOException {
        if (id < 0 || !map.containsKey(id)) {
            throw new GlobalException(INVALID_ID);
        }

        Path path = Paths.get(storeDir + File.separator + id + JSON);
        Files.delete(path);
        map.remove(id);
    }

}
