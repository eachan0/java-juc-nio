package nio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月09日
 * @desc TestChannel
 * * 一、通道（Channel）：用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 * *
 * * 二、通道的主要实现类
 * * 	java.nio.channels.Channel 接口：
 * * 		|--FileChannel
 * * 		|--SocketChannel
 * * 		|--ServerSocketChannel
 * * 		|--DatagramChannel
 * *
 * * 三、获取通道
 * * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * * 		本地 IO：
 * * 		FileInputStream/FileOutputStream
 * * 		RandomAccessFile
 * *
 * * 		网络IO：
 * * 		Socket
 * * 		ServerSocket
 * * 		DatagramSocket
 * *
 * * 2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 * * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 * *
 * * 四、通道之间的数据传输
 * * transferFrom()
 * * transferTo()
 * *
 * * 五、分散(Scatter)与聚集(Gather)
 * * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 * *
 * * 六、字符集：Charset
 * * 编码：字符串 -> 字节数组
 * * 解码：字节数组  -> 字符串
 */
public class TestChannel {

    //利用通道完成文件的复制（非直接缓冲区）
    @Test
    public void test1() throws IOException {
        FileInputStream fis = new FileInputStream("1.jpg");
        FileOutputStream fos = new FileOutputStream("2.jpg");

        FileChannel fic = fis.getChannel();
        FileChannel foc = fos.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (fic.read(buf) != -1) {
            buf.flip();
            foc.write(buf);
            buf.clear();
        }

        fic.close();
        foc.close();
        fis.close();
        fos.close();
    }

    //使用直接缓冲区完成文件的复制(内存映射文件)
    @Test
    public void test2() throws IOException {
        FileChannel frc = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel fwc = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        MappedByteBuffer imb = frc.map(FileChannel.MapMode.READ_ONLY, 0, frc.size());
        MappedByteBuffer omb = fwc.map(FileChannel.MapMode.READ_WRITE, 0, frc.size());

        byte[] dst = new byte[imb.limit()];

        imb.get(dst);
        omb.put(dst);

        frc.close();
        fwc.close();
    }

    //通道之间的数据传输(直接缓冲区)
    @Test
    public void test3() throws IOException {
        FileChannel frc = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel fwc = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);


//        frc.transferTo(0, frc.size(), fwc);
        fwc.transferFrom(frc, 0, frc.size());

        frc.close();
        fwc.close();
    }

    //分散和聚集
    @Test
    public void test4() throws IOException {
        RandomAccessFile rf = new RandomAccessFile("1.txt", "rw");
        RandomAccessFile wf = new RandomAccessFile("2.txt", "rw");
        FileChannel inChannel = rf.getChannel();
        ByteBuffer bu1 = ByteBuffer.allocate(100);
        ByteBuffer bu2 = ByteBuffer.allocate(1024);
        ByteBuffer[] bus = {bu1, bu2};
        inChannel.read(bus);
        for (ByteBuffer byteBuffer : bus) {
            byteBuffer.flip();
        }

        System.out.println(new String(bus[0].array(), 0, bus[0].limit()));
        System.out.println("************************");
        System.out.println(new String(bus[1].array(), 0, bus[1].limit()));

        FileChannel outChannel = wf.getChannel();
        outChannel.write(bus);
        inChannel.close();
        outChannel.close();
    }

    @Test
    public void test5() {
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> entries = charsets.entrySet();
        for (Map.Entry<String, Charset> entry : entries) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    //字符集
    @Test
    public void test6() throws CharacterCodingException {
        Charset charset = StandardCharsets.UTF_8;
        CharsetEncoder encoder = charset.newEncoder();
        CharsetDecoder decoder = charset.newDecoder();

        CharBuffer cbuf = CharBuffer.allocate(1024);

        cbuf.put("中华人民共和国");
        cbuf.flip();

        ByteBuffer buf = encoder.encode(cbuf);

        for (var i = 0; i < buf.limit(); i++) {
            System.out.println(buf.get());
        }
        buf.flip();
        CharBuffer decode = decoder.decode(buf);
        System.out.println(decode.toString());
        System.out.println("******************************");

        CharsetDecoder decoder1 = StandardCharsets.ISO_8859_1.newDecoder();
        buf.flip();
        CharBuffer decode1 = decoder1.decode(buf);
        System.out.println(decode1.toString());
    }
}
