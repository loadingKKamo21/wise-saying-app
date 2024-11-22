package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.example.controllers.WiseSayingController;
import org.example.dto.WiseSayingReq;
import org.example.entities.WiseSaying;
import org.example.repositories.WiseSayingRepository;
import org.example.repositories.WiseSayingRepositoryImpl;
import org.example.services.WiseSayingService;
import org.example.services.WiseSayingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppTest {

    static final String TEST_STORE_DIR = "./db_test/wiseSaying";
    static final String JSON_EXT = ".json";
    static final String TXT_EXT = ".txt";

    WiseSayingRepository repository;
    WiseSayingService service;
    WiseSayingController controller;
    App app;

    BufferedReader br;
    BufferedWriter bw;

    @BeforeEach
    void before() {
        repository = new WiseSayingRepositoryImpl(TEST_STORE_DIR);
        service = new WiseSayingServiceImpl(repository);
    }

    @AfterEach
    void after() {
        deleteAll();
    }

    private void deleteAll() {
        try {
            Files.walkFileTree(Paths.get(TEST_STORE_DIR), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString();
                    if (fileName.endsWith(JSON_EXT) || fileName.endsWith(TXT_EXT)) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createDummy(final int size) {
        try {
            for (int i = 1; i <= size; i++) {
                WiseSayingReq req = WiseSayingReq.of("작가" + i, "명언" + i);
                repository.saveToJson(req);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("등록")
    void 등록() throws IOException {
        //Given
        String input = """
                등록
                명언 입력 테스트
                작가 입력 테스트
                종료
                """;

        br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        controller = new WiseSayingController(service, br, bw);

        app = new App(controller, br, bw);

        //When
        app.run();

        //Then
        WiseSaying wiseSaying = repository.loadFromJson(TEST_STORE_DIR + File.separator + 1 + JSON_EXT);

        assertThat(wiseSaying.getId()).isEqualTo(1);
        assertThat(wiseSaying.getContent()).isEqualTo("명언 입력 테스트");
        assertThat(wiseSaying.getAuthor()).isEqualTo("작가 입력 테스트");

        String output = sw.toString();

        assertThat(output).contains(1 + "번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록")
    void 목록() throws IOException {
        //Given
        createDummy(20);
        String input = """
                목록?keyword=1&keywordType=content
                종료
                """;

        br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        controller = new WiseSayingController(service, br, bw);

        app = new App(controller, br, bw);

        //When
        app.run();

        //Then
        String output = sw.toString();

        assertThat(output).contains("번호 / 작가 / 명언")
                .contains("19 / 작가19 / 명언19")
                .contains("18 / 작가18 / 명언18")
                .contains("17 / 작가17 / 명언17")
                .contains("16 / 작가16 / 명언16")
                .contains("15 / 작가15 / 명언15")
                .contains("페이지 : [1] 2 3");
    }

    @Test
    @DisplayName("삭제")
    void 삭제() throws IOException {
        //Given
        createDummy(10);
        String input = """
                삭제?id=5
                종료
                """;

        br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        controller = new WiseSayingController(service, br, bw);

        app = new App(controller, br, bw);

        //When
        app.run();

        //Then
        String output = sw.toString();

        assertThat(output).contains("5번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("수정")
    void 수정() throws IOException {
        //Given
        createDummy(10);
        String input = """
                수정?id=3
                명언 수정 테스트
                작가 수정 테스트
                종료
                """;

        br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        controller = new WiseSayingController(service, br, bw);

        app = new App(controller, br, bw);

        //When
        app.run();

        //Then
        WiseSaying wiseSaying = repository.loadFromJson(TEST_STORE_DIR + File.separator + 3 + JSON_EXT);

        assertThat(wiseSaying.getId()).isEqualTo(3);
        assertThat(wiseSaying.getContent()).isEqualTo("명언 수정 테스트");
        assertThat(wiseSaying.getAuthor()).isEqualTo("작가 수정 테스트");

        String output = sw.toString();

        assertThat(output).contains("명언3").contains("작가3").contains("3번 명언이 수정되었습니다.");
    }

    @Test
    @DisplayName("빌드")
    void 빌드() throws IOException {
        //Given
        createDummy(10);
        String input = """
                빌드
                종료
                """;

        br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        controller = new WiseSayingController(service, br, bw);

        app = new App(controller, br, bw);

        //When
        app.run();

        //Then
        File data = new File(TEST_STORE_DIR + File.separator + "data" + JSON_EXT);

        assertThat(data.exists()).isTrue();

        String output = sw.toString();

        assertThat(output).contains("data.json 파일의 내용이 갱신되었습니다.");
    }

    @Test
    @DisplayName("잘못된 명령어")
    void 실패() throws IOException {
        //Given
        String input = """
                등록?
                목록?keywordType=id
                삭제?id=-2
                수정?author=작가3
                빌드!
                종료
                """;

        br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        bw = new BufferedWriter(sw);
        controller = new WiseSayingController(service, br, bw);

        app = new App(controller, br, bw);

        //When
        app.run();

        //Then
        String output = sw.toString();

        assertThat(output).contains("'등록?'는(은) 올바르지 않은 명령입니다.")
                .contains("'목록?keywordType=id'는(은) 올바르지 않은 명령입니다.")
                .contains("'-2'는(은) ID가 아닙니다.")
                .contains("'수정?author=작가3'는(은) 올바르지 않은 명령입니다.")
                .contains("'빌드!'는(은) 올바르지 않은 명령입니다.");
    }

}