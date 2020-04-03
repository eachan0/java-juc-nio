package juc;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc juc.TestProductAndConsumer 虚假唤醒
 */
public class TestProductAndConsumer {
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Product product = new Product(clerk);
        Consumer consumer = new Consumer(clerk);
        new Thread(product,"生产者A").start();
        new Thread(product,"生产者B").start();
        new Thread(consumer,"消费者A").start();
        new Thread(consumer,"消费者B").start();
    }
}


class Clerk {

    private int product = 0;

    public synchronized void get() {
//        if (product >= 1) {
        while (product >= 1) {
            System.out.println("产品已满");
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println(Thread.currentThread().getName() + " : " + ++product);
        this.notifyAll();

    }

    public synchronized void sale(){
//        if (product<=0){
        while (product<=0){
            System.out.println("缺货");
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println(Thread.currentThread().getName() + " : " + --product);
        this.notifyAll();

    }
}

class Product implements Runnable{

    private Clerk clerk;

    public Product(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (var i =0 ;i<20;i++){
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
            }
            clerk.get();
        }
    }
}

class Consumer implements Runnable{

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (var i =0 ;i<20;i++){
            clerk.sale();
        }
    }
}
