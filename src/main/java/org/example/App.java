package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.controllers.WiseSayingController;

public class App {

    private static final String PRINT_REGEX = "^목록(?:\\?(?:(keywordType=(content|author)|keyword=([^&]+)|page=(\\d+))(?:&|$)){0,3})?$";
    private static final BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(System.out));
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static WiseSayingController controller = new WiseSayingController(BW, BR);

    static void run() throws IOException {
        controller.makeDirectory();
        controller.loadLastId();
        controller.loadAll();

        Pattern pattern = Pattern.compile(PRINT_REGEX);

        BW.write("== 명언 앱 ==");
        BW.newLine();
        do {
            controller.saveLastId();

            BW.write("명령) ");
            BW.flush();
            String cmd = BR.readLine();

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
                    BW.write("'" + str + "'는(은) 숫자가 아닙니다.");
                    BW.newLine();
                    BW.flush();
                } else {
                    controller.deleteWiseSaying(Integer.parseInt(str));
                }
            } else if (cmd.startsWith("수정?id=")) {
                String str = cmd.substring(cmd.indexOf("=") + 1);
                if (!str.matches("\\d+")) {
                    BW.write("'" + str + "'는(은) 숫자가 아닙니다.");
                    BW.newLine();
                    BW.flush();
                } else {
                    controller.editWiseSaying(Integer.parseInt(str));
                }
            } else if ("빌드".equals(cmd)) {
                controller.buildWiseSaying();
            } else {
                BW.write("'" + cmd + "'는(은) 올바르지 않은 명령입니다.");
                BW.newLine();
                BW.flush();
            }
        } while (true);

        BW.close();
        BR.close();
    }

}
