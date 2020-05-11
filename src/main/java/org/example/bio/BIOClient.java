package org.example.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(2)
public class BIOClient implements CommandLineRunner
{
    private final static String hostname = "127.0.0.1";
    private final int port = 26666;

    @Override
    public void run(String... args)
    {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                log.info("BIOClient>> begin");
                for (int loop = 0; loop < 3; loop ++) {
                    connectToServer();
                    TimeUnit.SECONDS.sleep(1);
                }
                log.info("BIOClient>> exit");
            } catch (Exception e) {
                log.info("BIOClient {}>> error", Thread.currentThread().getName());
            }
        }).start();
    }

    public void connectToServer() {
        try (Socket socket = new Socket(hostname, port); OutputStream outputStream = socket.getOutputStream();) {
            int count = 0;
            while (count < 3) {
                outputStream.write(String.format("BIOClient {}>> This is the %d-th message", count).getBytes());
                outputStream.flush();
                TimeUnit.SECONDS.sleep(1);
                count++;
            }
            log.info("BIOClient {}>>  exit", Thread.currentThread().getName());
        } catch (Exception ex) {
            log.info("BIOClient {}>> error, error message = {}", Thread.currentThread().getName(), ex.getMessage());
        }
    }
}
