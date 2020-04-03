package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc juc.TestAlternately  交替打印ABC
 */
public class TestAlternately {
    public static void main(String[] args) {

        AlternatelyDemo alternatelyDemo = new AlternatelyDemo();

        new Thread(() -> {
            for (var i= 0;i<15;i++){
                alternatelyDemo.loopA();
            }
        },"print-A").start();

        new Thread(() -> {
            for (var i= 0;i<15;i++){
                alternatelyDemo.loopB();
            }
        },"print-B").start();

        new Thread(() -> {
            for (var i= 0;i<15;i++){
                alternatelyDemo.loopC();
            }
        },"print-C").start();
    }
}
class AlternatelyDemo{

    private int pId = 1;

    private Lock lock = new ReentrantLock();

    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void loopA(){
        lock.lock();
        try {
            while (pId != 1){
                try {
                    conditionA.await();
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println(Thread.currentThread().getName()+": A");
            pId = 2;
            conditionB.signal();
        }finally {
            lock.unlock();
        }
    }

    public void loopB(){
        lock.lock();
        try {
            while (pId != 2){
                try {
                    conditionB.await();
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println(Thread.currentThread().getName()+": B");
            pId = 3;
            conditionC.signal();
        }finally {
            lock.unlock();
        }
    }

    public void loopC(){
        lock.lock();
        try {
            while (pId != 3){
                try {
                    conditionC.await();
                } catch (InterruptedException ignored) {
                }
            }
            System.out.println(Thread.currentThread().getName()+": C");
            System.out.println("------------------------");
            pId = 1;
            conditionA.signal();
        }finally {
            lock.unlock();
        }
    }
}
