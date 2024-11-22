package org.example.repositories;

import java.io.IOException;
import java.util.Map;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;

public interface WiseSayingRepository {

    void makeDirectory();

    WiseSaying saveToJson(WiseSayingReq param) throws IOException;

    WiseSaying update(int id, WiseSayingReq param) throws IOException;

    void buildData(String param) throws IOException;

    WiseSaying loadFromJson(String filePath) throws IOException;

    Map<Integer, WiseSaying> loadAll() throws IOException;

    void saveLastIdTxt() throws IOException;

    boolean hasLastId();

    int loadLastId();

    void deleteJson(int id) throws IOException;

}
