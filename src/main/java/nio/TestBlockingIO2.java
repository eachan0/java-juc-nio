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

        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        FileChannel fileChannel = FileChannel.open(Path.of("1.jpg"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            clientChannel.write(buffer);
            buffer.clear();
        }

        clientChannel.shutdownOutput();
        var i = 0;
        while ((i = clientChannel.read(buffer)) != -1) {
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, i));
            buffer.clear();
        }

        fileChannel.close();
        clientChannel.close();
    }

    @Test
    public void server() throws IOException {


        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        FileChannel fileChannel = FileChannel.open(Path.of("7.jpg"), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);

        serverChannel.bind(new InetSocketAddress(9898));

        SocketChannel clientChannel = serverChannel.accept();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (clientChannel.read(buffer) != -1) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        buffer.put("我已经接收到消息".getBytes());
        buffer.flip();
        clientChannel.write(buffer);

        fileChannel.close();
        clientChannel.close();
        serverChannel.close();
    }
}
