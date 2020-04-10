package nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月10日
 * @desc TestNewIO
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
 *
 *  idea 可能出现控制台无法输入的情况,解决办法
 *  1.改为 main 函数,
 *  2.修改idea启动配置文件 加上一行 -Deditable.java.test.console=true ,重启idea(2020.1亲测有效)
 */
public class TestNewIO {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void client() throws IOException {

        //获取客户机通道
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 配置为非阻塞式
        clientChannel.configureBlocking(false);

        //分配缓冲器
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 获取数据发送给服务端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((LocalDateTime.now().format(DATE_TIME_FORMATTER) + " : " + next).getBytes());
            buffer.flip();
            clientChannel.write(buffer);
            buffer.clear();
        }

        clientChannel.close();

    }

    @Test
    public void server() throws IOException {

        // 获取服务器通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        // 配置非阻塞
        serverChannel.configureBlocking(false);

        // 绑定端口
        serverChannel.bind(new InetSocketAddress(9898));

        // 获取选择器
        Selector selector = Selector.open();

        // 将通道注册到选择器上, 并且指定“监听接收事件”
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 轮询式的获取选择器上已经“准备就绪”的事件
        while (selector.select()>0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                if (key.isAcceptable()){
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector,SelectionKey.OP_READ);
                }else if (key.isReadable()){

                    // 获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel = (SocketChannel) key.channel();

                    // 读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    var len = 0;
                    while((len = sChannel.read(buf)) > 0 ){
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }
                it.remove();
            }
        }
    }
}
