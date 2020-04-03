package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc juc.TestProductAndConsumer 虚假唤醒
 */
public class TestProductAndConsumerByLock {
    public static void main(String[] args) {
        ClerkByLock clerk = new ClerkByLock();
        ProductByLock product = new ProductByLock(clerk);
        ConsumerByLock consumer = new ConsumerByLock(clerk);
        new Thread(product,"生产者A").start();
        new Thread(product,"生产者B").start();
        new Thread(consumer,"消费者A").start();
        new Thread(consumer,"消费者B").start();
    }
}


class ClerkByLock {

    private int product = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void get() {
        lock.lock();
        try {
            while (product >= 1) {
                System.out.println("产品已满");
                try {
                    condition.await();
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println(Thread.currentThread().getName() + " : " + ++product);
            condition.signalAll();
        }finally {
            lock.unlock();
        }

    }

    public void sale(){
        lock.lock();
        try {
            while (product<=0){
                System.out.println("缺货");
                try {
                    condition.await();
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println(Thread.currentThread().getName() + " : " + --product);
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
}

class ProductByLock implements Runnable{

    private ClerkByLock clerk;

    public ProductByLock(ClerkByLock clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (var i =0 ;i<20;i++){
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException ignored) {
            }
            clerk.get();
        }
    }
}

class ConsumerByLock implements Runnable{

    private ClerkByLock clerk;

    public ConsumerByLock(ClerkByLock clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (var i =0 ;i<20;i++){
            clerk.sale();
        }
    }
}
