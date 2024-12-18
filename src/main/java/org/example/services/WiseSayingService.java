package org.example.services;

import java.io.IOException;
import java.util.Map;
import org.example.dto.WiseSayingPaging;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;

public interface WiseSayingService {

    void makeDirectory();

    int saveWiseSaying(WiseSayingReq dto) throws IOException;

    boolean updateWiseSaying(int id, WiseSayingReq dto) throws IOException;

    void buildData() throws IOException;

    WiseSaying loadById(int id) throws IOException;

    Map<Integer, WiseSaying> loadAll() throws IOException;

    void saveLastId() throws IOException;

    int loadLastId() throws IOException;

    boolean deleteWiseSaying(int id) throws IOException;

    WiseSayingPaging getPaging(String keywordType, String keyword, int page) throws IOException;

}
