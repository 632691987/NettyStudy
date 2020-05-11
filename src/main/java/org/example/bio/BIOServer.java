package org.example.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 这个模型的问题在于，
 * 1，每个请求都需要建立独立的线程
 * 2，当并发较多的时候，就会建立大量的线程
 */
@Component
@Slf4j
@Order(1)
public class BIOServer implements CommandLineRunner
{
    @Override
    public void run(String... args)
    {
        new Thread(() -> {
            try {
                ExecutorService executorService = Executors.newCachedThreadPool();
                ServerSocket serverSocket = new ServerSocket(26666);

                log.info("BIOServer<< started");
                int count = 0;
                while (count < 3)
                {
                    final Socket socket = serverSocket.accept();
                    log.info("BIOServer<< new connection created");
                    executorService.execute(() -> handler(socket));
                    count++;
                }

                executorService.shutdown();
                while (!executorService.isTerminated())
                {
                    TimeUnit.SECONDS.sleep(10);
                }
                log.info("BIOServer<< Server shutdown");
            } catch (Exception e) {
                log.info("BIOServer {}<< error", Thread.currentThread().getName());
            }
        }).start();
    }

    public static void handler(Socket socket)
    {
        try
        {
            byte[] bytes;
            InputStream inputStream = socket.getInputStream();
            //Read client sending data in loop
            while (true)
            {
                bytes = new byte[1024];
                int read = inputStream.read(bytes);
                if (read != -1)
                {
                    String message = new String(bytes, 0, read);
                    log.info("BIOServer<< {} Received {} bytes message: {}", Thread.currentThread().getName(), read, message);
                    //Arrays.fill(bytes, (byte)0);//reset all the byte array, so no redundant data left
                }
                else
                {
                    log.info("BIOServer<< {} No more data, exit", Thread.currentThread().getName());
                    break;
                }
            }
        }
        catch (Exception e)
        {
            log.warn(e.getMessage());
        }
        finally
        {
            closeSocket(socket);
            log.info("BIOServer<< Close client connection");
        }
    }

    private static void closeSocket(Socket socket)
    {
        if (socket == null)
        {
            return;
        }
        try
        {
            socket.close();
        }
        catch (Throwable t)
        {
            log.info("BIOServer<< Error ocurred while closing ");
        }
    }
}
