package org.example.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.example.dto.WiseSayingPaging;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;
import org.example.proxy.LoggingProxy;
import org.example.services.WiseSayingService;
import org.example.services.WiseSayingServiceImpl;

public class WiseSayingController {

    private final BufferedWriter bw;
    private final BufferedReader br;

    public WiseSayingController(final BufferedWriter bw, final BufferedReader br) {
        this.bw = bw;
        this.br = br;
    }

    private WiseSayingService service = LoggingProxy.createProxy(new WiseSayingServiceImpl(), WiseSayingService.class);

    public void makeDirectory() {
        service.makeDirectory();
    }

    public void saveWiseSaying() throws IOException {
        String content;
        do {
            bw.write("명언: ");
            bw.flush();
            content = br.readLine();
            inputEmptyCheck(content);
        } while (content.trim().isEmpty());

        String author;
        do {
            bw.write("작가: ");
            bw.flush();
            author = br.readLine();
            inputEmptyCheck(author);
        } while (author.trim().isEmpty());

        WiseSayingReq dto = WiseSayingReq.of(author, content);
        int id = service.saveWiseSaying(dto);

        bw.write(id + "번 명언이 등록되었습니다.");
        bw.newLine();

        bw.flush();
    }

    public void printWiseSaying(final String keywordType, final String keyword, final int page) throws IOException {
        WiseSayingPaging wiseSayingPaging = service.getPaging(keywordType, keyword, page);

        int currentPage = wiseSayingPaging.getCurrentPage();
        int totalPages = wiseSayingPaging.getTotalPages();
        List<WiseSaying> content = wiseSayingPaging.getContent();

        if (content.isEmpty()) {
            bw.write("저장된 명언이 없습니다.");
            bw.newLine();
            bw.flush();
            return;
        }

        bw.write("번호 / 작가 / 명언");
        bw.newLine();
        bw.write("----------------------");
        bw.newLine();

        for (WiseSaying wiseSaying : content) {
            bw.write(wiseSaying.getId() + " / " + wiseSaying.getAuthor() + " / " + wiseSaying.getContent());
            bw.newLine();
        }

        bw.write("----------------------");
        bw.newLine();

        bw.write("페이지 : ");
        for (int i = 1; i <= totalPages; i++) {
            if (i == currentPage) {
                bw.write("[" + i + "] ");
            } else {
                bw.write(i + " ");
            }
        }
        bw.newLine();

        bw.flush();
    }

    public void deleteWiseSaying(final int id) throws IOException {
        Map<Integer, WiseSaying> map = service.loadAll();

        if (map.containsKey(id)) {
            service.deleteWiseSaying(id);
            bw.write(id + "번 명언이 삭제되었습니다.");
            bw.newLine();
        } else {
            bw.write(id + "번 명언이 존재하지 않습니다.");
            bw.newLine();
        }

        bw.flush();
    }

    public void editWiseSaying(final int id) throws IOException {
        Map<Integer, WiseSaying> map = service.loadAll();

        if (map.containsKey(id)) {
            WiseSaying wiseSaying = map.get(id);

            bw.write("명언(기존): " + wiseSaying.getContent());
            bw.newLine();

            String content;
            do {
                bw.write("명언: ");
                bw.flush();
                content = br.readLine();
                inputEmptyCheck(content);
            } while (content.trim().isEmpty());

            bw.write("작가(기존): " + wiseSaying.getAuthor());
            bw.newLine();

            String author;
            do {
                bw.write("작가: ");
                bw.flush();
                author = br.readLine();
                inputEmptyCheck(author);
            } while (author.trim().isEmpty());

            service.deleteWiseSaying(id);
            WiseSayingReq req = WiseSayingReq.of(author, content);
            service.updateWiseSaying(id, req);
        } else {
            bw.write(id + "번 명언은 존재하지 않습니다.");
            bw.newLine();
        }

        bw.flush();
    }

    public void buildWiseSaying() throws IOException {
        Map<Integer, WiseSaying> map = service.loadAll();

        if (map.isEmpty()) {
            bw.write("데이터가 존재하지 않아 빌드할 수 없습니다.");
            bw.newLine();
        } else {
            service.buildData();

            bw.write("data.json 파일의 내용이 갱신되었습니다.");
            bw.newLine();
        }

        bw.flush();
    }

    public void saveLastId() throws IOException {
        service.saveLastId();
    }

    public void loadLastId() throws IOException {
        service.loadLastId();
    }

    public void loadAll() throws IOException {
        service.loadAll();
    }

    private void inputEmptyCheck(final String input) throws IOException {
        if (input.trim().isEmpty()) {
            bw.write("공백이 아닌 내용을 입력해주세요.");
            bw.newLine();
        }
    }

}
