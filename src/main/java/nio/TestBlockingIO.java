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

        //获取客户端通道
        SocketChannel cChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取本地文件
        FileChannel fileChannel = FileChannel.open(Path.of("1.jpg"), StandardOpenOption.READ);
        while (fileChannel.read(buffer) != -1) {
            buffer.flip();

            // 发送给服务端
            cChannel.write(buffer);
            buffer.clear();
        }
        fileChannel.close();
        cChannel.close();
    }

    @Test
    public void server() throws IOException {

        // 获取服务端通道
        ServerSocketChannel sChannel = ServerSocketChannel.open();

        // 绑定端口
        sChannel.bind(new InetSocketAddress(9898));

        // 等待并获取客户端通道
        SocketChannel cChannel = sChannel.accept();

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 接收数据保存
        FileChannel fileChannel = FileChannel.open(Path.of("6.jpg"), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
        while (cChannel.read(buffer) != -1) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        // 关闭通道
        sChannel.close();
        cChannel.close();
        fileChannel.close();
    }
}
