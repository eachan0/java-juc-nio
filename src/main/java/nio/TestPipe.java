package nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月10日
 * @desc TestPipe
 */
public class TestPipe {

    @Test public void test() throws IOException {
        //获取管道
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("我是你爸爸".getBytes());
        buffer.flip();
        sink.write(buffer);

        buffer.clear();

        Pipe.SourceChannel source = pipe.source();
        int read = source.read(buffer);
        System.out.println(new String(buffer.array(),0,read));

        sink.close();
        source.close();
    }
}
