<!-- GFM-TOC -->
* [单例模式](#单例模式)
    * [1.介绍](#1介绍)
    * [2.实现方式](#2实现方式)
        * [2.1 懒汉式](#21-懒汉式)
          * [2.1.1 线程不安全的懒汉式](#211-线程不安全的懒汉式) 
          * [2.1.2 线程安全的懒汉式](#212-线程安全的懒汉式)
        * [2.2 饿汉式](#22-饿汉式)
        * [2.3 静态内部类](#23-静态内部类)
        * [2.4 双重校验锁(DCL)](#23-双重校验锁dcl)
          * [2.4.1 双重锁机制分析](#231-双重锁机制分析)
          * [2.4.2 双重锁机制失效分析](#232-双重锁机制失效分析)
<!-- GFM-TOC -->

# 单例模式
```
  单例模式属于创建型模式,它提供了一种创建对象的最佳方式;
```
## 1.介绍
 -  **目的** :
```
  单例模式涉及到单一的类,该类负责创建自己的对象，同时确保只有单个对象被创建。
  这个类提供了一种访问其唯一对象的方式,可以直接访问,不需要实例化该类的对象;
```
 -  **主要解决** :
```
  一个全局使用的类频繁地创建与销毁
```
 -  **如何解决** :
```
  判断系统是否已经有这个实例化对象,如果有直接返回,如果没有则创建;
```
 -  **核心代码** :
```
  构造函数要求是私有的 private,这样该类就不会被实例化;
```
 -  **优点** :
```
  [1]在内存中只有一个实例,减少了内存开销,尤其是频繁地创建和销毁实例
  [2]避免对资源的多重占用[TODO 解释];
```
## 2.实现方式
### 2.1 懒汉式
#### 2.1.1 线程不安全的懒汉式
 - 实现代码:
```
  public class Singleton {

    private static Singleton instance = null;

    /**
     * 私有化构造器,这样该类就不会被实例化;
     */
    private Singleton() {

    }

    public static Singleton getInstance() {
        //判断是否为空,如果是创建实例,如果不是直接返回;
        if (instance == null) {
          // JVM 重排序导致的空指针问题;
          // 1.在堆中开辟内存空间
          // 2.在堆内存中实例化Singleton();
          // 3.把对象指向堆空间;
          // 由于jvm存在乱序执行功能，所以可能在2还没执行时就先执行了3，如果此时再被切换到线程B上，由于执行了3,INSTANCE 已经非空了，// 会被直接拿出来用，这样的话，就会出现异常。这个就是著名的DCL失效问题。 
            instance = new Singleton();
        }
        return instance;
    }
}
```
 - 特性:
   -  **延迟加载** 
   -  **非线程安全** :
``` 
  多线程访问getInstance()方法都会得到实例,从严格意义上来讲,它不算单例模式;
```
#### 2.1.2 线程安全的懒汉式
 - 实现代码:
```
  public class Singleton {
    private static Singleton instance = null;
    /**
     * 私有化构造器,这样该类就不会被实例化;
     * ;
     */
    private Singleton() {

    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
``` 
 - 特性:
   -  **延迟加载** 
   -  **线程安全** 
   -  **效率低下** : 必须加锁 synchronized 才能保证单例，但加锁会影响效率
   -  **避免内存浪费** :第一次调用才初始化,避免了内存浪费;

### 2.2 饿汉式
 - 实现代码:
```
  public class Singleton {

    private static Singleton instance = new Singleton();

    /**
     * 私有化构造器,这样该类就不会被实例化;
     * ;
     */
    private Singleton() {
    }

    public static  Singleton getInstance() {
        return instance;
    }
}
```
 - 特性:
   -  **线程安全** :
   -  **存在内存泄漏的风险** 
```
  采用饿汉模式，单实例对象便会在类加载完成之时，常驻堆中，后续访问时本质上是通过该类的Class对象嵌入的intance指针寻址，找到单实例对象的所在。
  这一模式的好处在于：
  1、通过空间换时间，避免了后续访问时由于对象的构造带来的时间上的开销；
  2、(WHY) 无需考虑多线程的并发问题，JVM在类加载过程中，会通过内部加锁机制保证加载类的全局唯一性。
  不好的地方，就是不管你用还是不用，只要完成了类加载，Heap中单实例对象所占的内存空间就被占据了，
  某种程度上，也是内存泄漏的体现。这也是采用『饿汉模式』的由来。
```
### 2.3 静态内部类
 - 实现代码:
 ```
   public class Singleton {
    private Singleton() {

    }

    private static class SingleTonHolder {
        private static Singleton singleton = new Singleton();
    }

    private static Singleton getInstance() {
        return SingleTonHolder.singleton;
    }
}
 ```
  - 特性:
   - **外部类加载时并不需要立即加载内部类 ( 静态内部类和非静态内部类一样，都是在被调用时才会被加载 )，内部类不被加载则不去初始化INSTANCE，故而不占内存**
   - URL: https://blog.csdn.net/mnb65482/article/details/80458571
 
### 2.4 双重校验锁(DCL)
 - 实现代码:
```
  public class Singleton {

    // volatile 禁止指令重排序
    private static volatile Singleton instance;

    /**
     * 私有化构造器,这样该类就不会被实例化;
     */
    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                    return instance;
                }
            }
        }
        return instance;
    }
}
```
 - 特性
   -  **延迟加载** 
   -  **线程安全** 
   -  **JDK版本：1.5** 
   
#### 2.4.1 双重锁机制分析
 
- 为什么使用双重锁机制
```
  我们假设一种情况,就是当两个线程同时到达,即同时调用getInstance()方法.
  此时由于singleTon == null,所以很明显,两个线程都可以通过第一重的检查(instance==null);
  之后,由于锁机制的存在,所以只会有一个线程进入到临界区中,另一个线程只能在外面等待;
  而当第一个线程执行完new Singleton()语句之后,第二个线程便可以进去到临界区中;
  此时,如果没有第二道instance==null的判断,
  那么第二个线程还是可以调用new Singleton()语句去生成新的实例化对象
  ,这就违背了单例模式的初衷。
  
```
 - 假设将第一个判断去掉,是否会出现异常呢?
```
   当我们去掉第一个非空判断后,程序在多线程情况下还是可以完好的运行的;
   在不考虑第一个判断的情况下:
   当两个线程同时到达,由于锁机制的存在,第一个线程进入临界区之后去初始化new SingeTon并给instance赋值,第二个线程则等待,当第一个线程退出 lock 语句块时， singleTon 这个静态变量已不为 null 了，所以当第二个线程进入 lock 时,
   还是会被第二重 singleton == null 挡在外面，而无法执行 new Singleton（），
```
 - 既然在没有第一个判断的情况下,单例也可以实施,那为什么需要第一道判断呢?
 ```
这里就涉及一个性能问题了，因为对于单例模式的话，new SingleTon（）只需要执行一次就 OK 了，
而如果没有第一重 singleTon == null 的话，每一次有线程进入 getInstance（）时，均会执行锁定操作来实现线程同步，
这是非常耗费性能的，而如果我加上第一重 singleTon == null 的话，
那么就只有在第一次，也就是 singleTton ==null成立时的情况下执行一次锁定以实现线程同步，
而以后的话，便只要直接返回 Singleton 实例就 OK 了而根本无需再进入 lock 语句块了，这样就可以解决由线程同步带来的性能问题了。
 ```
 #### 2.4.2 双重锁机制失效分析
 
  - 双重校验锁失效原因分析:
```
   [1]我们先引入一个概念:指令重排序(具体会在多线程的锁机制中详解);
   所谓指令重排序是指在不改变 原语义的情况下,通过调整指令的执行顺序让程序执行地更快;
   [2]双重锁的问题在于 由于指令重排序的存在,导致初始化Singleton()的过程和将对象地址赋给instance字段的顺序是不确定的。
   在某个线程创建单例对象的过程中,在构造器被调用之前,就为该对象分配了内存空间并将对象的字段设置为默认值.此时,将分配的内存地址复制给instance字段[栈指针指向堆内存的过程],然而该对象还没有初始化。若与此同时,另外一个线程来调用getInstance()方法 ，那么导致的结果就是取到的不是正确的对象。
   [3] 在JDK 1.5版本之后引入了 volatile关键字.
   它对于我们来讲 ,实现了内存可见性,确保实例每次都会从主存中读取,以及禁止指令重排序
   也就是保证了instance变量被赋值的时候是确保已经被初始化过的;
```
