package nio;

import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc TestBuffer
 * *一、缓冲区（Buffer）：在 Java NIO 中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 * *
 * * 根据数据类型不同（boolean 除外），提供了相应类型的缓冲区：
 * * ByteBuffer
 * * CharBuffer
 * * ShortBuffer
 * * IntBuffer
 * * LongBuffer
 * * FloatBuffer
 * * DoubleBuffer
 * *
 * * 上述缓冲区的管理方式几乎一致，通过 allocate() 获取缓冲区
 * *
 * * 二、缓冲区存取数据的两个核心方法：
 * * put() : 存入数据到缓冲区中
 * * get() : 获取缓冲区中的数据
 * *
 * * 三、缓冲区中的四个核心属性：
 * * capacity : 容量，表示缓冲区中最大存储数据的容量。一旦声明不能改变。
 * * limit : 界限，表示缓冲区中可以操作数据的大小。（limit 后数据不能进行读写）
 * * position : 位置，表示缓冲区中正在操作数据的位置。
 * *
 * * mark : 标记，表示记录当前 position 的位置。可以通过 reset() 恢复到 mark 的位置
 * *
 * * 0 <= mark <= position <= limit <= capacity
 * *
 * * 四、直接缓冲区与非直接缓冲区：
 * * 非直接缓冲区：通过 allocate() 方法分配缓冲区，将缓冲区建立在 JVM 的内存中
 * * 直接缓冲区：通过 allocateDirect() 方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
 */
public class TestBuffer {
    @Test
    public void test1() {
        // 1.分配Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("--------------------allocate()");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 2.写数据
        byteBuffer.put("abcde".getBytes());
        System.out.println("--------------------put()");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 3.切换到读取数据模式
        byteBuffer.flip();
        System.out.println("--------------------flip()");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 4.读取
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst);

        System.out.println("get more : " + new String(dst));
        System.out.println("--------------------get()");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 5.重读 (position 指向初值)
        byteBuffer.rewind();
        System.out.println("--------------------rewind()");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 6.清空缓冲区, (只是指针重置,数据还在,处于遗忘状态,依然可以获取)
        byteBuffer.clear();
        System.out.println("--------------------clear()");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        System.out.println("get one : " + (char)(byteBuffer.get()));
    }

    @Test
    public void test2(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("abcde".getBytes());
        byteBuffer.flip();
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst,0,2);
        System.out.println("get-1 : "+new String(dst));
        System.out.println("p-1 : "+byteBuffer.position());
        byteBuffer.mark();
        byteBuffer.get(dst,2,2);
        System.out.println("get-2 : "+new String(dst));
        System.out.println("p-2 : "+byteBuffer.position());
        byteBuffer.reset();
        System.out.println("p-3 : "+byteBuffer.position());
        //判断缓冲区中是否还有剩余数据
        if(byteBuffer.hasRemaining()){
            //获取缓冲区中可以操作的数量
            System.out.println(byteBuffer.remaining());
        }
    }

    @Test
    public void test3(){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect());
    }
}
