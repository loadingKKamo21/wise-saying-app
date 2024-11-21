package org.example.repositories;

import java.io.IOException;
import java.util.Map;
import org.example.entities.WiseSaying;

public interface WiseSayingRepository {

    void makeDirectory();

    void saveToJson(WiseSaying param) throws IOException;

    void buildData(String param) throws IOException;

    WiseSaying loadFromJson(String filePath) throws IOException;

    Map<Integer, WiseSaying> loadAll(Map<Integer, WiseSaying> map) throws IOException;

    void saveLastIdTxt(int id) throws IOException;

    boolean hasLastId();

    int loadLastId() throws IOException;

    void deleteJson(int id) throws IOException;

}
