package singleton;

/**
 * 饿汉式
 */

public class Singleton02 {
    private static final Singleton02 instance = new Singleton02();
    private Singleton02(){

    }
    public static Singleton02 getInstance(){
        return instance;
    }
}
