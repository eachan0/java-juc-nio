package juc;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月02日
 * @desc juc.TestCollections
 *  * CopyOnWriteArrayList/CopyOnWriteArraySet : “写入并复制”
 *  * 注意：添加操作多时，效率低，因为每次添加时都会进行复制，开销非常的大。并发迭代操作多时可以选择。
 */
public class TestCollections {
    public static void main(String[] args) {
        ThreadList threadList = new ThreadList();
        for (var i = 0;i<10;i++){
            new Thread(threadList).start();
        }
    }
}

class ThreadList implements Runnable{
//    private static List<String> list = Collections.synchronizedList(new ArrayList<>());
    private static List<String> list = new CopyOnWriteArrayList<>();

    static {
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }
    @Override
    public void run() {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            list.add("11");
        }
    }
}
