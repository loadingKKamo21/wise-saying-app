package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.example.controllers.WiseSayingController;
import org.example.error.GlobalExceptionHandler;
import org.example.proxy.LoggingProxy;
import org.example.repositories.WiseSayingRepository;
import org.example.repositories.WiseSayingRepositoryImpl;
import org.example.services.WiseSayingService;
import org.example.services.WiseSayingServiceImpl;

public class Main {
    public static void main(String[] args) throws IOException {
        GlobalExceptionHandler.initialize();
        String storeDir = "./db/wiseSaying";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        WiseSayingRepository repository =
                LoggingProxy.createProxy(new WiseSayingRepositoryImpl(storeDir), WiseSayingRepository.class);
        WiseSayingService service =
                LoggingProxy.createProxy(new WiseSayingServiceImpl(repository), WiseSayingService.class);
        WiseSayingController controller = new WiseSayingController(service, br, bw);

        App app = new App(controller, br, bw);
        app.run();
    }
}