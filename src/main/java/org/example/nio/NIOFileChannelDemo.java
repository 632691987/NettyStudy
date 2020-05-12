package org.example.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NIOFileChannelDemo
{

    /**
     *
     * 0, Channel get from stream
     * 1, String to ByteBuffer
     * 2, ByteBuffer to channel
     *
     */
    public void writeFile()
    {
        String str = "hello world";
        String fileName = "NIOFileChannelDemo.sample1.txt";
        FileUtils.deleteQuietly(new File(fileName));
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName); FileChannel fileChannel = fileOutputStream.getChannel();)
        {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(str.getBytes());

            byteBuffer.flip();
            fileChannel.write(byteBuffer);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        finally
        {
            //FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Ignore
    public void readFile()
    {
        writeFile();
        String fileName = "NIOFileChannelDemo.sample1.txt";

        try (FileInputStream fileInputStream = new FileInputStream(fileName); FileChannel fileChannel = fileInputStream.getChannel();)
        {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            fileChannel.read(byteBuffer);

            byteBuffer.flip();
            log.info("File content: {}", new String(byteBuffer.array()));
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        finally
        {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    public void copyFile()
    {
        writeFile();
        String fileFrom = "NIOFileChannelDemo.sample1.txt";
        String fileTo = "NIOFileChannelDemo.sample3.txt";

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
            FileChannel outputChannel = fileOutputStream.getChannel();

            FileInputStream fileInputStream = new FileInputStream(fileFrom);
            FileChannel inputChannel = fileInputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (true)
            {
                byteBuffer.clear();
                int readBytes = inputChannel.read(byteBuffer);
                if (readBytes == -1)
                {
                    break;
                }
                byteBuffer.flip();
                outputChannel.write(byteBuffer);
            }

            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(fileInputStream);
            log.info("OK, everything is fine");
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        finally
        {
            FileUtils.deleteQuietly(new File(fileFrom));
            FileUtils.deleteQuietly(new File(fileTo));
        }
    }

    @Test
    public void copyFile2()
    {
        String fileFrom = "/home/vzhang/Downloads/SHA1.rmvb";
        String fileTo = "/home/vzhang/Downloads/SHA2.mp4";

        try
        {
            long startTime = System.currentTimeMillis();
            FileInputStream fileInputStream = new FileInputStream(fileFrom);
            FileOutputStream fileOutputStream = new FileOutputStream(fileTo);

            FileChannel sourceChannel = fileInputStream.getChannel();
            FileChannel destChannel = fileOutputStream.getChannel();

            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(fileInputStream);
            long endTime = System.currentTimeMillis();

            log.info("OK, everything is fine, time = {}", (endTime - startTime));
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        finally
        {
//            FileUtils.deleteQuietly(new File(fileFrom));
//            FileUtils.deleteQuietly(new File(fileTo));
        }
    }
}
