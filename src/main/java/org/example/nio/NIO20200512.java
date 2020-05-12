package org.example.nio;

import java.nio.IntBuffer;

import lombok.extern.slf4j.Slf4j;

/**
 * 举例说明 Buffer 的使用
 * Buffer 类四个属性：
 *
 * capacity: 在缓存区创建的时候设定，并且不能改变
 * limit   :
 * position: 当前位置
 * Mark    : 标记
 *
 */
@Slf4j
public class NIO20200512
{
    public static void main(String[] args) {

        // 创建一个 buffer, 能够存放 5 个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向 buffer 存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2 + 1);
        }

        // 从 buffer 读取数据

        // 将 buffer 转换模式，读写切换
        intBuffer.flip();

        {
            /**
             * 原本是 1 3 5 7 9
             * 设置了之后，就变成 3, 5 了
             */
            intBuffer.position(1);//不能超过capacity, 否则 IllegalArgumentException
            intBuffer.limit(3);// 不能超过capacity, 否则 IllegalArgumentException
        }


        /**
         * 原本 1 3 5 7 9
         */
        while (intBuffer.hasRemaining()) {
            log.info("Next data: {}", intBuffer.get());
        }
    }
}
