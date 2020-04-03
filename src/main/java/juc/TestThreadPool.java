package juc;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc juc.TestThreadPool 线程池
 */
public class TestThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {


        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        // Runnable
        /*juc.PoolDemo poolDemo = new juc.PoolDemo();
        for (var i = 0;i<10;i++){
            executor.submit(poolDemo);
        }*/

        // Callable
        List<Future<Integer>> list = new ArrayList<>();
        for (var j = 0; j < 10; j++) {
            Future<Integer> submit = executor.submit(() -> {
                int sum = 0;
                for (var i = 0; i < 101; i++) {
                    sum += i;
                }
                return sum;
            });
            list.add(submit);
        }
        list.forEach(x -> {
            try {
                System.out.println(x.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.shutdownNow();
    }
}

class PoolDemo implements Runnable {
    private int i = 0;

    @Override
    public void run() {
        while (i < 100) {
            System.out.println(Thread.currentThread().getName() + " : " + i++);
        }
    }
}
