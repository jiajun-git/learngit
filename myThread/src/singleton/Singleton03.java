package singleton;

/**
 * 静态内部类
 */

public class Singleton03 {
    private Singleton03(){

    }
    public static Singleton03 getInstance(){
        return StaticSingleton.instance;
    }
    private static class StaticSingleton {
        private static final Singleton03 instance = new Singleton03();
    }



}
