package juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc juc.TestReadWriteLock 读写锁
 * <p>
 * WW/RW 需要互斥
 * RR 不需要互斥
 */
public class TestReadWriteLock {
    public static void main(String[] args) {
        ReadWriteDemo readWriteDemo = new ReadWriteDemo();
        new Thread(() -> readWriteDemo.set((int) (Math.random() * 101)), "Write").start();

        for (var i = 0; i < 20; i++) {
            new Thread(() -> readWriteDemo.get(), "Read").start();
        }
    }
}

class ReadWriteDemo {
    private int num = 0;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void get() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " : " + num);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(int num) {
        lock.writeLock().lock();
        try {
            this.num = num;
            System.out.println(Thread.currentThread().getName() + " : " + num);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
