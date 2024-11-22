package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.controllers.WiseSayingController;

public class App {

    private static final String PRINT_REGEX = "^목록(?:\\?(?:(keywordType=(content|author)|keyword=([^&]+)|page=(\\d+))(?:&|$)){0,3})?$";

    private final WiseSayingController controller;
    private final BufferedReader br;
    private final BufferedWriter bw;

    public App(final WiseSayingController controller, final BufferedReader br, final BufferedWriter bw) {
        this.controller = controller;
        this.br = br;
        this.bw = bw;
    }

    void run() throws IOException {
        controller.makeDirectory();
        controller.loadLastId();
        controller.loadAll();

        Pattern pattern = Pattern.compile(PRINT_REGEX);

        bw.write("== 명언 앱 ==");
        bw.newLine();
        do {
            controller.saveLastId();

            bw.write("명령) ");
            bw.flush();
            String cmd = br.readLine();

            Matcher matcher = pattern.matcher(cmd);

            if ("종료".equals(cmd)) {
                break;
            } else if ("등록".equals(cmd)) {
                controller.saveWiseSaying();
            } else if (matcher.matches()) {
                String keywordType = matcher.group(2) != null
                        ? !matcher.group(2).trim().isEmpty()
                        ? matcher.group(2) : null : null;
                String keyword = matcher.group(3) != null
                        ? !matcher.group(3).trim().isEmpty()
                        ? matcher.group(3) : null : null;
                int page = matcher.group(4) != null
                        ? !matcher.group(4).trim().isEmpty()
                        ? Integer.parseInt(matcher.group(4)) : 1 : 1;

                controller.printWiseSaying(keywordType, keyword, page);
            } else if (cmd.startsWith("삭제?id=")) {
                String str = cmd.substring(cmd.indexOf("=") + 1);
                if (!str.matches("\\d+")) {
                    bw.write("'" + str + "'는(은) ID가 아닙니다.");
                    bw.newLine();
                    bw.flush();
                } else {
                    controller.deleteWiseSaying(Integer.parseInt(str));
                }
            } else if (cmd.startsWith("수정?id=")) {
                String str = cmd.substring(cmd.indexOf("=") + 1);
                if (!str.matches("\\d+")) {
                    bw.write("'" + str + "'는(은) ID가 아닙니다.");
                    bw.newLine();
                    bw.flush();
                } else {
                    controller.editWiseSaying(Integer.parseInt(str));
                }
            } else if ("빌드".equals(cmd)) {
                controller.buildWiseSaying();
            } else {
                bw.write("'" + cmd + "'는(은) 올바르지 않은 명령입니다.");
                bw.newLine();
                bw.flush();
            }
        } while (true);

        bw.close();
        br.close();
    }

}
