package singleton;

/**
 *单例模式——懒汉式
 **/
public class Singleton01 implements Runnable {
    public Singleton01() {
        System.out.println("Singleton01 is create");
    }

    private static Singleton01 instance = null;

    /**
    * 1.适用于单线程环境
    * */
    public static Singleton01 getInstanceA() {
        if(null==instance){
            instance = new Singleton01();
        }
        return instance;
    }

    /**
     * 适用于多线程，加锁
     */
    public  static synchronized Singleton01 getInstanceB(){
        if (instance == null) {
            instance = new Singleton01();
        }
        return instance;
    }

    /**
     * 双重检查加锁DCL
     */
    public static Singleton01 getInstanceC(){
        // 先判断实例是否存在，若不存在再对类对象进行加锁处理
        if (instance == null) {
            synchronized (Singleton01.class) {
                if (instance == null) {
                    instance = new Singleton01();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Singleton01.getInstanceA();
        }
        System.out.println(System.currentTimeMillis() - beginTime);
    }
}
