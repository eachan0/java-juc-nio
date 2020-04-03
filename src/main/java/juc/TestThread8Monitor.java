package juc;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月03日
 * @desc juc.TestThread8Monitor 线程 8 锁
 *  1. 两个普通同步方法，两个线程，标准打印， 打印? //one  two
 *  2. 新增 Thread.sleep() 给 getOne() ,打印? //one  two
 *  3. 新增普通方法 getThree() , 打印? //three  one   two
 *  4. 两个普通同步方法，两个 Number 对象，打印?  //two  one
 *  5. 修改 getOne() 为静态同步方法，打印?  //two   one
 *  6. 修改两个方法均为静态同步方法，一个 Number 对象?  //one   two
 *  7. 一个静态同步方法，一个非静态同步方法，两个 Number 对象?  //two  one
 *  8. 两个静态同步方法，两个 Number 对象?   //one  two
 *
 *  线程八锁的关键：
 *  ①非静态方法的锁默认为  this,  静态方法的锁为 对应的 Class 实例
 *  ②某一个时刻内，只能有一个线程持有锁，无论几个方法。
 */
public class TestThread8Monitor {

    public static void main(String[] args) {
        PrintNumber printNumber = new PrintNumber();
        PrintNumber printNumber2 = new PrintNumber();
        new Thread(() -> printNumber.printA()).start();
        new Thread(() -> printNumber2.printB()).start();
//        new Thread(() -> printNumber.printC()).start();
    }
}

class PrintNumber {
    public static synchronized void printA() {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        System.out.println("one");
    }

    public static synchronized void printB() {
        System.out.println("two");
    }

    public void printC() {
        System.out.println("three");
    }
}
