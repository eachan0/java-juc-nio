package juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月02日
 * @desc juc.TestCallable 创建线程的第三种方式 : 实现callable接口
 * 共4种
 * 1.继承 Thread
 * 2.实现 Runnable
 * 3.实现 Callable (有返回值)
 * 4.线程池
 */
public class TestCallable {
    public static void main(String[] args) {
        CallableDemo demo = new CallableDemo();
        FutureTask<Integer> result = new FutureTask<>(demo);
        new Thread(result).start();
        try {
            Integer sum = result.get();
            System.out.println(sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (var i = 0; i < 10; i++) {
            sum += i;
        }
        return sum;
    }
}
