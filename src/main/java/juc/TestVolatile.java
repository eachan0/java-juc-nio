package juc;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月02日
 * @desc juc.ThreadDemo
 *  *
 *  * 一、volatile 关键字：当多个线程进行操作共享数据时，可以保证内存中的数据可见。
 *  * 					  相较于 synchronized 是一种较为轻量级的同步策略。
 *  *
 *  * 注意：
 *  * 1. volatile 不具备“互斥性”
 *  * 2. volatile 不能保证变量的“原子性”
 */
public class TestVolatile {
    public static void main(String[] args) {
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo).start();

        while (true){
//            synchronized (threadDemo){
                if (threadDemo.isFlag()){
                    System.out.println("*************************");
                    break;
                }
//            }
        }
    }
}
class ThreadDemo implements Runnable{

    volatile private boolean flag = false;

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("flag : "+isFlag());
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
