package nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月10日
 * @desc TestNewIO2
 * * 一、使用 NIO 完成网络通信的三个核心：
 *  *
 *  * 1. 通道（Channel）：负责连接
 *  *
 *  * 	   java.nio.channels.Channel 接口：
 *  * 			|--SelectableChannel
 *  * 				|--SocketChannel
 *  * 				|--ServerSocketChannel
 *  * 				|--DatagramChannel
 *  *
 *  * 				|--Pipe.SinkChannel
 *  * 				|--Pipe.SourceChannel
 *  *
 *  * 2. 缓冲区（Buffer）：负责数据的存取
 *  *
 *  * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 */
public class TestNewIO2 {
    @Test
    public void send() throws IOException {
        DatagramChannel dc = DatagramChannel.open();

        dc.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        Scanner scan = new Scanner(System.in);

        while(scan.hasNext()){
            String str = scan.next();
            buf.put((new Date().toString() + ":\n" + str).getBytes());
            buf.flip();
            dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
            buf.clear();
        }

        dc.close();
    }

    @Test
    public void receive() throws IOException{
        DatagramChannel dc = DatagramChannel.open();

        dc.configureBlocking(false);

        dc.bind(new InetSocketAddress(9898));

        Selector selector = Selector.open();

        dc.register(selector, SelectionKey.OP_READ);

        while(selector.select() > 0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while(it.hasNext()){
                SelectionKey sk = it.next();

                if(sk.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    dc.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            }

            it.remove();
        }
    }
}
