package org.example;

import org.example.controllers.DataController;

import java.io.IOException;
import java.util.Scanner;

import static org.example.Main.App.run;

public class Main {
    
    public static void main(String[] args) throws IOException {
        run();
    }
    
    static class App {
        
        private static final DataController CONTROLLER = new DataController();
        
        static void run() throws IOException {
            CONTROLLER.makeDirectory();
            CONTROLLER.loadAllData();
            
            Scanner sc = new Scanner(System.in);
            System.out.println("== 명언 앱 ==");
            do {
                System.out.print("명령) ");
                String command = sc.nextLine();
                
                if ("종료".equals(command))
                    break;
                else if ("등록".equals(command))
                    CONTROLLER.saveWiseSaying();
                else if ("목록".equals(command))
                    CONTROLLER.printWiseSaying();
                else if (command.startsWith("삭제?id=")) {
                    String str = command.substring(command.indexOf("=") + 1);
                    if (!str.matches("\\d+"))
                        System.out.println("'" + str + "'는(은) 숫자가 아닙니다.");
                    else
                        CONTROLLER.deleteWiseSaying(Integer.parseInt(str));
                } else if (command.startsWith("수정?id=")) {
                    String str = command.substring(command.indexOf("=") + 1);
                    if (!str.matches("\\d+"))
                        System.out.println("'" + str + "'는(은) 숫자가 아닙니다.");
                    else
                        CONTROLLER.editWiseSaying(Integer.parseInt(str));
                } else if ("빌드".equals(command))
                    CONTROLLER.buildWiseSaying();
                else
                    System.out.println("'" + command + "'는(은) 올바르지 않은 명령입니다.");
            } while (true);
            sc.close();
            
            CONTROLLER.saveLastId();
        }
        
    }
    
}