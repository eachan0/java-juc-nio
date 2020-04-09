package nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月09日
 * @desc TestBlockingIO
 * * 一、使用 NIO 完成网络通信的三个核心：
 * *
 * * 1. 通道（Channel）：负责连接
 * *
 * * 	   java.nio.channels.Channel 接口：
 * * 			|--SelectableChannel
 * * 				|--SocketChannel
 * * 				|--ServerSocketChannel
 * * 				|--DatagramChannel
 * *
 * * 				|--Pipe.SinkChannel
 * * 				|--Pipe.SourceChannel
 * *
 * * 2. 缓冲区（Buffer）：负责数据的存取
 * *
 * * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 */
public class TestBlockingIO {

    @Test
    public void client() throws IOException {
        SocketChannel cChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        FileChannel fileChannel = FileChannel.open(Path.of("1.jpg"), StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            cChannel.write(buffer);
            buffer.clear();
        }
        fileChannel.close();
        cChannel.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel sChannel = ServerSocketChannel.open();

        FileChannel fileChannel = FileChannel.open(Path.of("6.jpg"), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);

        sChannel.bind(new InetSocketAddress(9898));

        SocketChannel cChannel = sChannel.accept();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (cChannel.read(buffer) != -1) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        sChannel.close();
        cChannel.close();
        fileChannel.close();
    }
}
