package juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月02日
 * @desc juc.TestLock 用于解决多线程安全问题的第三种方式
 * 共三种
 * 1.synchronized(隐式锁) 同步方法
 * 2.synchronized(隐式锁) 同步代码块
 * 3.同步锁 Lock, 一种显示锁,lock()/unlock() 上/释放锁
 */
public class TestLock {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(ticket, "1号窗口").start();
        new Thread(ticket, "2号窗口").start();
        new Thread(ticket, "3号窗口").start();
        new Thread(ticket, "4号窗口").start();
    }

}

class Ticket implements Runnable {

    private int tick = 100;
//    private AtomicInteger tick = new AtomicInteger(100);
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                Thread.sleep(50);
                if (tick > 0){
                    System.out.println(Thread.currentThread().getName() + "完成售票,余票为: " + --tick);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}
