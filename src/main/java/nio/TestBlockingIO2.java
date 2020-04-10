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
 * @desc TestBlockingIO2
 */
public class TestBlockingIO2 {


    @Test
    public void client() throws IOException {

        //获取客户端通道
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取本地文件
        FileChannel fileChannel = FileChannel.open(Path.of("1.jpg"), StandardOpenOption.READ);
        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            // 发送到服务器
            clientChannel.write(buffer);
            buffer.clear();
        }

        // 关闭客户端通道写入连接
        clientChannel.shutdownOutput();

        // 接收服务器反馈
        var i = 0;
        while ((i = clientChannel.read(buffer)) != -1) {
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, i));
            buffer.clear();
        }

        // 关闭通道
        fileChannel.close();
        clientChannel.close();
    }

    @Test
    public void server() throws IOException {

        //获取服务器通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        //绑定端口
        serverChannel.bind(new InetSocketAddress(9898));

        // 等待并获取客户端通道
        SocketChannel clientChannel = serverChannel.accept();

        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 接收文件保存到本地
        FileChannel fileChannel = FileChannel.open(Path.of("7.jpg"), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
        while (clientChannel.read(buffer) != -1) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        // 向客户端发送发聩信息
        buffer.put("我已经接收到消息".getBytes());
        buffer.flip();
        clientChannel.write(buffer);

        // 关闭通道
        fileChannel.close();
        clientChannel.close();
        serverChannel.close();
    }
}
