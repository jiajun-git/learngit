## Java

<https://snailclimb.top/JavaGuide/#/?id=%e5%ae%b9%e5%99%a8>

### 1.transient

**Java中对象的序列化**:指的是将对象转换成以字节序列的形式来表示，这些字节序列包含了对象的数据和信息，一个序列化后的对象可以被写到数据库或文件中，也可用于网络传输，一般当我们使用缓存cache（内存空间不够有可能会本地存储到硬盘）或远程调用rpc（网络传输）的时候，经常需要让我们的实体类实现***Serializable***接口，目的就是为了让其可序列化。当然，序列化后的最终目的是为了反序列化，恢复成原先的Java对象，要不然序列化后干嘛呢，所以序列化后的字节序列都是可以恢复成Java对象的，这个过程就是反序列化。

序列化：将一个对象转换成一串二进制表示的字节数组，通过保存或转移这些字节数据来达到持久化的目的。

反序列化：将字节数组重新构造成对象

Java中**transient**关键字的作用，简单地说，就是让某些被修饰的成员属性变量不被序列化，这一看好像很好理解，就是不被序列化，那么什么情况下，一个对象的某些字段不需要被序列化呢？如果有如下情况，可以考虑使用关键字transient修饰：

1、类中的字段值可以根据其它字段推导出来，如一个长方形类有三个属性：长度、宽度、面积（示例而已，一般不会这样设计），那么在序列化的时候，面积这个属性就没必要被序列化了；

2、其它，看具体业务需求吧，哪些字段不想被序列化.为什么要不被序列化呢，主要是为了节省存储空间，其它的感觉没啥好处，可能还有坏处（有些字段可能需要重新计算，初始化什么的），总的来说，**利大于弊**。

```sh
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @description 使用transient关键字不序列化某个变量
 *        注意读取的时候，读取数据的顺序一定要和存放数据的顺序保持一致
 *        
 * @author Alexia
 * @date  2013-10-15
 */
public class TransientTest {
    
    public static void main(String[] args) {
        
        User user = new User();
        user.setUsername("Alexia");
        user.setPasswd("123456");
        
        System.out.println("read before Serializable: ");
        System.out.println("username: " + user.getUsername());
        System.err.println("password: " + user.getPasswd());
        
        try {
            ObjectOutputStream os = new ObjectOutputStream(
                    new FileOutputStream("C:/user.txt"));
            os.writeObject(user); // 将User对象写进文件
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(
                    "C:/user.txt"));
            user = (User) is.readObject(); // 从流中读取User的数据
            is.close();
            
            System.out.println("\nread after Serializable: ");
            System.out.println("username: " + user.getUsername());
            System.err.println("password: " + user.getPasswd());
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 8294180014912103005L;  
    
    private String username;
    private transient String passwd;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswd() {
        return passwd;
    }
    
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
```

输出为：

```sh
read before Serializable: 
username: Alexia
password: 123456

read after Serializable: 
username: Alexia
password: null      //密码字段为null，说明反序列化时根本没有从文件中获取到信息。
```



### 2.Java中Ear、Jar、War文件之间有何不同

在文件结构上，三者并没有什么不同，它们都采用zip或jar档案文件压缩格式。但是它们的使用目的有所区别：

　　**Jar文件**（扩展名为. Jar）包含Java类的普通库、资源（resources）、辅助文件（auxiliary files）等

　　**War文件**（扩展名为.War）包含全部Web应用程序。在这种情形下，一个Web应用程序被定义为单独的一组文件、类和资源，用户可以对jar文件进行封装，并把它作为小型服务程序（servlet）来访问。

　　**Ear文件**（扩展名为.Ear）包含全部企业应用程序。在这种情形下，一个企业应用程序被定义为多个jar文件、资源、类和Web应用程序的集合。

　　每一种文件（.jar, .war, .ear）只能由应用服务器（application servers）、小型服务程序容器（servlet containers）、EJB容器（EJB containers）等进行处理

jar--封装类

war--封装web站点

ear--封装ejb。

它们的关系具体为：

jar:是java Achieve--按java格式压缩的类包，包含内容 class、properties文件，是文件封装的最小单元 级别：**小**

war:是file web Achieve--包含Servlet、JSP页面、JSP标记库、JAR库文件HTML/XML文档和其他公用资源文件，如图片、音频文件等 级别：**中**

ear:是 file Enterprise Achieve--除了包含JAR、WAR以外，还包括EJB组件   部署文件 application-client.xml web.xml application.xml    级别：**大**

ear包：企业级应用，通常是EJB打成ear包。 ear是J2ee的应用文件的扩展名 是Enterprise Application Achiever， 是打包了的企业应用程序，里面可包含war(web Application archiever),ejb等 ear包：企业级应用，通常是EJB打成ear包。 war包:是做好一个web应用后，通常是网站，打成包部署到容器中。 jar包：通常是开发时要引用通用类，打成包便于存放管理。

### 3.maven

##### **maven的结构：**

src -main             -java                 -package     -test             -java                 -package     -resources

mvn clean install -Dmaven.test.skip=true    跳过测试



##### **maven 打包方式**：

如果使用[Java](http://lib.csdn.net/base/javase) -jar xxx.jar执行运行jar文件，会出现"no main manifest attribute, in xxx.jar"（没有设置Main-Class）、ClassNotFoundException（找不到依赖包）等错误。

要想jar包能直接通过java -jar xxx.jar运行，需要满足：

1、在jar包中的META-INF/MANIFEST.MF中指定Main-Class，这样才能确定程序的入口在哪里；

​      （       1.打包时指定了主类，可以直接用java -jar {xxx.jar}。
​                 2.打包时没有指定主类，可以用java -cp {xxx.jar} {主类名称（绝对路径）}。
​                 3.要引用其他的jar包，可以用java -{[classpath|cp]} {$CLASSPATH}:{xxxx.jar} {主类名称（绝对路  径）}。其中 -classpath 指定需要引入的类。

​        ）

2、要能加载到依赖包。

使用Maven有以下几种方法可以生成能直接运行的jar包，可以根据需要选择一种合适的方法。

```sh
方法一：使用maven-jar-plugin和maven-dependency-plugin插件打包
在pom.xml中配置：

<build>  
    <plugins>  
  
        <plugin>  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-jar-plugin</artifactId>  
            <version>2.6</version>  
            <configuration>  
                <archive>  
                    <manifest>  
                        <addClasspath>true</addClasspath>  
                        <classpathPrefix>lib/</classpathPrefix>  
                        <mainClass>com.xxg.Main</mainClass>  
                    </manifest>  
                </archive>  
            </configuration>  
        </plugin>  
        <plugin>  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-dependency-plugin</artifactId>  
            <version>2.10</version>  
            <executions>  
                <execution>  
                    <id>copy-dependencies</id>  
                    <phase>package</phase>  
                    <goals>  
                        <goal>copy-dependencies</goal>  
                    </goals>  
                    <configuration>  
                        <outputDirectory>${project.build.directory}/lib</outputDirectory>  
                    </configuration>  
                </execution>  
            </executions>  
        </plugin>  
  
    </plugins>  
</build>  
```

```sh
方法二：使用maven-assembly-plugin插件打包
在pom.xml中配置：

<build>  
    <plugins>  
  
        <plugin>  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-assembly-plugin</artifactId>  
            <version>2.5.5</version>  
            <configuration>  
                <archive>  
                    <manifest>  
                        <mainClass>com.xxg.Main</mainClass>  
                    </manifest>  
                </archive>  
                <descriptorRefs>  
                    <descriptorRef>jar-with-dependencies</descriptorRef>  
                </descriptorRefs>  
            </configuration>  
        </plugin>  
  
    </plugins>  
</build>  
```

```sh
方法三：使用maven-shade-plugin插件打包
在pom.xml中配置：

<build>  
    <plugins>  
  
        <plugin>  
            <groupId>org.apache.maven.plugins</groupId>  
            <artifactId>maven-shade-plugin</artifactId>  
            <version>2.4.1</version>  
            <executions>  
                <execution>  
                    <phase>package</phase>  
                    <goals>  
                        <goal>shade</goal>  
                    </goals>  
                    <configuration>  
                        <transformers>  
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">  
                                <mainClass>com.xxg.Main</mainClass>  
                            </transformer>  
                        </transformers>  
                    </configuration>  
                </execution>  
            </executions>  
        </plugin>  
  
    </plugins>  
</build>  
```



**项目样本** 打包成为   stcp-1.0-SNAPSHOT-jar-with-dependencies.jar

```sh
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.demo</groupId>
    <artifactId>stcp</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.42.Final</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.2</version>
                <configuration>
                    <optimize>true</optimize>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase> <!-- bind to the packaging phase -->
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

        </plugins>
    </build>
</project>
```

### 4.java类的定义

```
1.定义位置：文件中、其他类中（内部类）
2.内部类：成员内部类、内嵌内部类（static修饰）、局部内部类（定义在方法中）、匿名内部类（没有类名）
在java中，类最常见的定义位置是文件中，一个文件中可以定义多个类，但是只能有一个public的类，而且java文件名必须和这个public类相同。看看下面代码
package com.senmu.pack_a
//TestA.java
public class TestA{}
class TestB{}
class TestC{}
这里有一个TestA.java的源文件，里面定义了三个class。可以看出一个源文件里能定义多个class，但是有且只能有一个public类，非public类的名字只要符合java标识符规则就可以，public类的名字必须和源文件名一致。至于为什么有这个规定，很多网上的帖子都说是为了方便JVM根据文件名找到main函数入口，个人觉得这种说法不太可信也不太合理。原因如下，JVM读取的是编译后的.class文件而不是.java源文件，而定义在一个源文件中的多个类编译后都生成了由各自类名命名的.class文件。在我看来当初java的设计者这样规定的主要目应该是为了给源码阅读提供便利，试想一下如果一个源文件中可以有多个public的class而且名字与源文件名不一致，那么当我们在阅读代码时想要了解某个引用类的定义情况应该去哪找这个类的源代码呢？当有了这个规定之后我们就可以根据import的包名和类名准确找到源文件的位置。至于源文件中的其他非public类，他们只能被本包内的类使用，使用范围有限，命名也就没有严格要求了。
类除了可以定义在文件中还可以定义在其他类中。定义在其他类中的类成为内部类，内部类又可以分成成员内部类和内嵌内部类，其区别在于是否被static修饰符修饰。成员内部类可以访问外部类的所有成员属性和方法，内嵌内部类和普通类没有什么区别只是加了外部类命名限定而已。如下面代码。
package com.senmu.pack_a
//TestA.java
public class TestA{
    private int f1;
    private int f2;

    public TestA(int f1,int f2){
        this.f1 = f1;
        this.f2 = f2;
    }
    public int cal{
        return new TestAInner1().cal();//在外部类中可以像普通类一样使用
    }
    public class TestAInner1{
        public int cal(){
            return f1+f2;//成员内部类可以直接访问外部类的所有公有及私有属性。
        }
    }
    public static class TestInner2{
        public int cal(){
            //return f1+f2 编译错误，因为内嵌内部类不能访问外部类的成员属性。
        return 0;
        }
    }
}

package com.senmu.pack_b
import com.senmu.pack_a.TestA;
//TestB.java
public class TestB{
    private TestA.TestAInner ti = new TestA.TestAInner();//TestA.TestInner是内嵌内部类，可以和普通类一样使用
    public void test(){
        TestA ta = new TestA();
        ta.new TestInner1().cal();//在其他类中使用TestA的成员内部类。

    }
}
最后，java中的类还可以定义在方法中（局部内部类），定义在方法中的类只能在方法中使用，可以看到所有对方法可见的变量。
package com.senmu.pack_a
//TestA.java
public class TestA{
    private int f1;
    private int f2;

    public TestA(int f1,int f2){
        this.f1 = f1;
        this.f2 = f2;
    }
    public int cal(int a){
        class MethodInner{
            public int test(){
                return a + f1 +f2;//方法中内部类可以访问对方法可见所以变量
            }
        }
        return new MethodInner().test();//方法中的内部类只能在本方法中使用。
}
```

### 5.构造方法

```
实例在创建时通过new操作符会调用其对应的构造方法，构造方法用于初始化实例；

没有定义构造方法时，编译器会自动创建一个默认的无参数构造方法；

可以定义多个构造方法，编译器根据参数自动判断；

可以在一个构造方法内部调用另一个构造方法，便于代码复用。

构造器可以有任何访问的修饰符，public、private、protected或者没有修饰符，都可以对构造方法进行修饰。不同于实例方法的是构造方法不能有任何非访问性质的修饰符修饰，例如static、final、synchronized、abstract等都不能修饰构造方法。
```

### 6.方法重载和重写

```
重写(Override)与重载(Overload)
http://www.runoob.com/java/java-override-overload.html
```

### 7.面向对象（封装、继承、多态）

https://www.cnblogs.com/hysum/p/7100874.html

##### **封装**

```sh
#封装：
就是把对象的属性和行为（数据）结合为一个独立的整体，并尽可能隐藏对象的内部实现细节，就是把不想告诉或者不该告诉别人的东西隐藏起来，通过该类提供的方法来实现对隐藏信息的操作和访问。
```

　![img](https://images2015.cnblogs.com/blog/1189312/201706/1189312-20170630170717493-357592353.png)

```sh
对封装的属性不一定要通过get/set方法，其他方法也可以对封装的属性进行操作。当然最好使用get/set方法.
```

```sh
1.访问修饰符：
  访问权限   类   包  子类  其他包
  public     ∨   ∨    ∨    ∨          （对任何人都是可用的）
  protect    ∨   ∨   ∨    ×　  （继承的类可以访问以及和private一样的权限）
  default    ∨   ∨   ×     ×　　　 （包访问权限，即在整个包内均可被访问）
  private    ∨   ×   ×     ×　　　 （除类型创建者和类型的内部方法之外的任何人都不能访问的元素）
  
从上到下封装性越来越好
```

```sh
2.this 关键字
java 内部类 内部类提供了更好的封装，可以把内部类隐藏在外部类之内，不允许同一个包中的其他类访问该类。
```

##### **继承**

```sh
#继承   extends
继承是类与类的一种关系，是一种“is a”的关系。比如“狗”继承“动物”，这里动物类是狗类的父类或者基类，狗类是动物类的子类或者派生类。
java中的继承是单继承，即一个类只有一个父类。
子类拥有父类的所有属性和方法（除了private修饰的属性不能拥有）从而实现了实现代码的复用
```

```sh
1.子类如果对继承的父类的方法不满意（不适合），可以自己编写继承的方法，这种方式就称为方法的重写。当调用方法时会优先调用子类的方法。
　重写要注意：
　  a、返回值类型
　　b、方法名
　　c、参数类型及个数
　都要与父类继承的方法相同，才叫方法的重写。
```

```sh
2.继承的初始化顺序:
java程序的执行顺序是：
父类对象属性初始化---->父类对象构造方法---->子类对象属性初始化--->子类对象构造方法
```

```sh
3.final
final 修饰类，则该类不允许被继承。final类中的所有成员方法都会被隐式地指定为final方法。
final 修饰方法，则该方法不允许被覆盖(重写)。
final 修饰属性，则该类的该属性不会进行隐式的初始化，所以 该final 属性的初始化属性必须有值，或在构造方法中赋值(但只能选其一，且必须选其一，因为没有默认值！)，且初始化之后就不能改了，只能赋值一次。
final 修饰变量，则该变量的值只能赋一次值，在声明变量的时候才能赋值，即变为常量。
```

```sh
4.super关键字
　在对象的内部使用，可以代表父类对象。
　　1、访问父类的属性：super.age
　　2、访问父类的方法：super.eat()
　super的应用：
　首先我们知道子类的构造的过程当中必须调用父类的构造方法。其实这个过程已经隐式地使用了我们的super关键字。
　这是因为如果子类的构造方法中没有显示调用父类的构造方法，则系统默认调用父类无参的构造方法。
　那么如果自己用super关键字在子类里调用父类的构造方法，则必须在子类的构造方法中的第一行。
　要注意的是：如果子类构造方法中既没有显示调用父类的构造方法，而父类没有无参的构造方法，则编译出错。
```

##### **多态**

```sh
#多态
那么什么是多态呢？多态就是对象的多种形态。（针对某个类型的方法调用，其真正执行的方法取决于运行时期实际类型的方法。）
多态表现在两个方面：
①：引用多态　　　
　　父类的引用可以指向本类的对象；
　　父类的引用可以指向子类的对象；
```

![img](https://images2015.cnblogs.com/blog/1189312/201707/1189312-20170701155536086-1897896282.png)

 ```sh
我们不能使用一个子类的引用来指向父类的对象:Dog obj3 = new Animal()
②：方法多态
　　根据上述创建的两个对象：本类对象和子类对象，同样都是父类的引用，当我们指向不同的对象时，它们调用的方法也是多态的。
　　创建本类对象时，调用的方法为本类方法；
　　创建子类对象时，调用的方法为子类重写的方法或者继承的方法；
　　使用多态的时候要注意：如果我们在子类中编写一个独有的方法（没有继承父类的方法），此时就不能通过父类的引用创建的子类对象来调用该方法！！！
　　注意： 继承是多态的基础。(Java中有两种形式可以实现多态：继承（多个子类对同一方法的重写）和接口（实现接口并覆盖接口中同一方法）。)
 ```

```sh
1.引用类型转换
$ 向上类型转换(隐式/自动类型转换)，是小类型转换到大类型。
Dog dog = new Dog();
Animal animal = dog;//自动类型提升，向上转型
$向下类型转换(强制类型转换)，是大类型转换到小类型(有风险,可能出现数据溢出)。
Dog dog = new Dog();
Animal animal = dog;
Dog dog2 = (Dog)animal;
$instanceof运算符，来解决引用对象的类型，避免类型转换的安全性问题。
instanceof是Java的一个二元操作符，和==，>，<是同一类东东。由于它是由字母组成的，所以也是Java的保留关键字。它的作用是测试它左边的对象是否是它右边的类的实例，返回boolean类型的数据。
if(animal instanceof Cat)//判断animal是不是Cat类型

补充说明：在比较一个对象是否和另一个对象属于同一个类实例的时候，我们通常可以采用instanceof和getClass两种方法通过两者是否相等来判断，但是两者在判断上面是有差别的。Instanceof进行类型检查规则是:你属于该类吗？或者你属于该类的派生类吗？而通过getClass获得类型信息采用==来进行检查是否相等的操作是严格的判断,不会存在继承方面的考虑；
```

```sh
2.抽象和接口见下
```

### 8.抽象

```sh
1. 抽象类不能被实例化(初学者很容易犯的错)，如果被实例化，就会报错，编译无法通过。只有抽象类的非抽象子类可以创建对象。
2. 抽象类中不一定包含抽象方法，但是有抽象方法的类必定是抽象类。
3. 抽象类中的抽象方法只是声明，不包含方法体，就是不给出方法的具体实现也就是方法的具体功能。
4. 构造方法，类方法（用 static 修饰的方法）不能声明为抽象方法。
5. 抽象类的子类必须给出抽象类中的抽象方法的具体实现，除非该子类也是抽象类。
#抽象和接口的区别
https://www.cnblogs.com/dolphin0520/p/3811437.html
```

### 9.接口

　　接口，英文称作interface，在软件工程中，接口泛指供别人调用的方法或者函数。从这里，我们可以体会到Java语言设计者的初衷，它是对行为的抽象。在Java中，定一个接口的形式如下：

```
`[``public``] ``interface` `InterfaceName {` `}`
```

　　接口中可以含有 变量和方法。但是要注意，接口中的变量会被隐式地指定为public static final变量（并且只能是public static final变量，用private修饰会报编译错误），而方法会被隐式地指定为public abstract方法且只能是public abstract方法（用其他关键字，比如private、protected、static、 final等修饰会报编译错误），并且接口中所有的方法不能有具体的实现，也就是说，接口中的方法必须都是抽象方法。从这里可以隐约看出接口和抽象类的区别，接口是一种极度抽象的类型，它比抽象类更加“抽象”，并且一般情况下不在接口中定义变量。

　　要让一个类遵循某组特地的接口需要使用implements关键字，具体格式如下：

```
`class` `ClassName ``implements` `Interface1,Interface2,[....]{``}`
```

　　可以看出，**允许一个类遵循多个特定的接口**。如果一个非抽象类遵循了某个接口，就必须实现该接口中的所有方法。对于遵循某个接口的抽象类，可以不实现该接口中的抽象方法。

### 10.线程池及多线程

![Java线程的状态](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/19-1-29/Java%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%8A%B6%E6%80%81.png)

![Java线程状态变迁](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/19-1-29/Java%20%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81%E5%8F%98%E8%BF%81.png)



![https://www.runoob.com/wp-content/uploads/2014/01/java-thread.jpg](https://www.runoob.com/wp-content/uploads/2014/01/java-thread.jpg)

线程创建之后它将处于 **NEW（新建）** 状态，调用 `start()` 方法后开始运行，线程这时候处于 **READY（可运行）** 状态。可运行状态的线程获得了 cpu 时间片（timeslice）后就处于 **RUNNING（运行）** 状态。

> 操作系统隐藏 Java虚拟机（JVM）中的 READY 和 RUNNING 状态，它只能看到 RUNNABLE 状态（图源：[HowToDoInJava](https://howtodoinjava.com/)：[Java Thread Life Cycle and Thread States](https://howtodoinjava.com/java/multi-threading/java-thread-life-cycle-and-thread-states/)），所以 Java 系统一般将这两个状态统称为 **RUNNABLE（运行中）** 状态 。

当线程执行 `wait()`方法之后，线程进入 **WAITING（等待）**状态。进入等待状态的线程需要依靠其他线程的通知才能够返回到运行状态，而 **TIME_WAITING(超时等待)** 状态相当于在等待状态的基础上增加了超时限制，比如通过 `sleep（long millis）`方法或 `wait（long millis）`方法可以将 Java 线程置于 TIMED WAITING 状态。当超时时间到达后 Java 线程将会返回到 RUNNABLE 状态。当线程调用同步方法时，在没有获取到锁的情况下，线程将会进入到 **BLOCKED（阻塞）** 状态。线程在执行 Runnable 的`run()`方法之后将会进入到 **TERMINATED（终止）** 状态。

 ```sh
#抢占式调度
优先让优先级高的线程使用 CPU，如果线程的优先级相同，那么会随机选择一个(线程随机性)，Java使用的为抢占式调度。
 ```

```sh
#运用多线程会出现的常见问题
1.当有全局性的参数的时候，需要为每个线程单独深度复制一个参数对象，不然在一个线程中对参数的修改可能会影响其他线程中参数的值，而造成间歇性的错误，不容易排查；
2.死锁问题--是当两个线程互相等待获取对方的对象监视器时就会发生死锁。一旦出现死锁，整个程序既不会出现异常也不会有提示，但所有线程都处于阻塞状态。死锁一般出现于多个同步监视器的情况。
（1）两个线程里面分别持有两个Object对象：lock1和lock2。这两个lock作为同步代码块的锁；
（2）线程1的run()方法中同步代码块先获取lock1的对象锁，Thread.sleep(xxx)，时间不需要太多，50毫秒差不多了，然后接着获取lock2的对象锁。这么做主要是为了防止线程1启动一下子就连续获得了lock1和lock2两个对象的对象锁
（3）线程2的run)(方法中同步代码块先获取lock2的对象锁，接着获取lock1的对象锁，当然这时lock1的对象锁已经被线程1锁持有，线程2肯定是要等待线程1释放lock1的对象锁的
这样，线程1"睡觉"睡完，线程2已经获取了lock2的对象锁了，线程1此时尝试获取lock2的对象锁，便被阻塞，此时一个死锁就形成了。
```



##### 代码演示

```sh
#继承Thread
public class Dog extends Thread{
    @Override
    public void run() {
         for (int i=2;i<100;i++){
             System.out.println("dog run " + i + " steps");
         }
    }
}

public class Running {
    public static void main(String[] args){
        Cat cat = new Cat();
        Dog dog = new Dog();
        dog.start();
        cat.start();
        for(int i=2;i<100;i++){
            System.out.println("main run " + i + " steps");
        }
    }
}

#实现Runnable接口
public class Apple implements Runnable {
    @Override
    public void run() {
        for (int i=1; i<=11; i++) {
            System.out.println(Thread.currentThread().getName()+" will buy iphone" + i);
        }
    }
}

public class Running {
    public static void main(String[] args) {
        //创建实例对象
        Apple apple = new Apple();
        //创建多线程
        Thread thread1 = new Thread(apple, "Bob");
        Thread thread2 = new Thread(apple, "Alice");
        Thread thread3 = new Thread(apple);

        thread1.start();
        thread2.start();
        for (int i=1;i<31;i++){
            System.out.println("main will buy huaweiP"+i);
        }
    }
}
```

##### 静态代理

```sh
使用一个代理对象将对象包装起来，然后用该代理对象来取代该对象，任何对原始对象的调用都要通过代理，代理对象决定是否以及何时调用原始对象的方法。
静态模式要求被代理类和代理类同时实现相应的一套接口，通过代理类调用重写接口的方法，实际上调用的是原始对象的同样的方法。
实现Runnable接口创建线程就是静态代理。Apple类和Thread类都实现了Runnable接口。

静态：由程序员创建代理类或特定工具自动生成源代码再对其编译。在程序运行前代理类的.class文件就已经存在了。
动态：在程序运行时运用反射机制动态创建而成。
```

```sh
静态代理类优缺点
优点：
代理使客户端不需要知道实现类是什么，怎么做的，而客户端只需知道代理即可（解耦合）
缺点：
1）代理类和委托类实现了相同的接口，代理类通过委托类实现了相同的方法。这样就出现了大量的代码重复。如果接口增加一个方法，除了所有实现类需要实现这个方法外，所有代理类也需要实现此方法。增加了代码维护的复杂度。
2）代理对象只服务于一种类型的对象，如果要服务多类型的对象。势必要为每一种对象都进行代理，静态代理在程序规模稍大时就无法胜任了。

举例说明：代理可以对实现类进行统一的管理，如在调用具体实现类之前，需要打印日志等信息，这样我们只需要添加一个代理类，在代理类中添加打印日志的功能，然后调用实现类，这样就避免了修改具体实现类。满足我们所说的开闭原则。但是如果想让每个实现类都添加打印日志的功能的话，就需要添加多个代理类，以及代理类中各个方法都需要添加打印日志功能（如上的代理方法中删除，修改，以及查询都需要添加上打印日志的功能）

即静态代理类只能为特定的接口(Service)服务。如想要为多个接口服务则需要建立很多个代理类。

引入动态代理：
根据如上的介绍，你会发现每个代理类只能为一个接口服务，这样程序开发中必然会产生许多的代理类
所以我们就会想办法可以通过一个代理类完成全部的代理功能，那么我们就需要用动态代理
```

##### 单例模式

```sh
单例模式是最简单的设计模式之一，属于创建型模式，它提供了一种创建对象的方式，确保只有单个对象被创建。这个设计模式主要目的是想在整个系统中只能出现类的一个实例，即一个类只有一个对象。
单例模式的解决的痛点就是节约资源，从两个方面看:
1.由于频繁使用的对象，可以省略创建对象所花费的时间，这对于那些重量级的对象而言，是很重要的.
2.因为不需要频繁创建对象，我们的GC压力也减轻了，而在GC中会有STW(stop the world)，从这一方面也节约了GC的时间
单例模式的缺点：简单的单例模式设计开发都比较简单，但是复杂的单例模式需要考虑线程安全等并发问题，引入了部分复杂度。
```

|            | 线程安全 | 并发性能好 | 可以延迟加载 | 序列化/反序列化安全 | 防反射攻击 |
| ---------- | :------: | :--------: | :----------: | :-----------------: | :--------: |
| 饿汉式     |    Y     |     Y      |              |                     |            |
| 懒汉不加锁 |          |     Y      |      Y       |                     |            |
| 懒汉加锁   |    Y     |            |      Y       |                     |            |
| DCL        |    Y     |     Y      |      Y       |                     |            |
| 静态内部类 |    Y     |     Y      |      Y       |                     |            |
| 枚举       |    Y     |     Y      |              |          Y          |     Y      |

```sh
从实现方式来讲他们最大的区别就是懒汉式是延时加载,
他是在需要的时候才创建对象,而饿汉式在加载类时创建实例。 
饿汉式无需关注多线程问题、写法简单明了、能用则用。但是它是加载类时创建实例、所以如果是一个工厂模式、缓存了很多实例、那么就得考虑效率问题，因为这个类一加载则把所有实例不管用不用一块创建。
懒汉式的优点是延时加载、缺点是应该用同步。

单例模式——饿汉式
public class Singleton2 {

    private static final Singleton2 instance = new Singleton2();

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return instance;
    }
}

懒汉式
public class Singleton1 {
    private static Singleton1 instance = null;
    private Singleton1() {
    }

    /**
     * 1、适用于单线程环境（不推荐）
     */
    public static Singleton1 getInstanceA() {
        if (null == instance) {
            instance = new Singleton1();
        }
        return instance;
    }

    /**
     * 2、适用于多线程环境，但效率不高（不推荐）
     */
    public static synchronized Singleton1 getInstanceB() {
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }

    /**
     * 3、双重检查加锁（推荐）DCL
     */
    public static Singleton1 getInstanceC() {
        // 先判断实例是否存在，若不存在再对类对象进行加锁处理
        if (instance == null) {
            synchronized (Singleton1.class) {
                if (instance == null) {
                    instance = new Singleton1();
                }
            }
        }
        return instance;
    }
}

静态内部类
public class StaticSingleton {
    /**
     * 私有构造方法，禁止在其他类中创建实例
     */
    private StaticSingleton() {
    }

    /**
     * 获取实例
     */
    public static StaticSingleton getInstance() {
        return StaticSingletonHolder.instance;
    }

    /**
     * 一个私有的静态内部类，用于初始化一个静态final实例
     */
    private static class StaticSingletonHolder {
        private static final StaticSingleton instance = new StaticSingleton();
    }

    /**
     * 方法A
     */
    public void methodA() {
    }

    /**
     * 方法B
     */
    public void methodB() {
    }

    public static void main(String[] args) {
        StaticSingleton.getInstance().methodA();
        StaticSingleton.getInstance().methodB();
    }
}

枚举
public class Singleton {
    public static void main(String[] args) {
        Single single = Single.SINGLE;
        single.print();
    }

    enum Single {
        SINGLE;

        private Single() {
        }

        public void print() {
            System.out.println("hello world");
        }
    }
}
```



### 11.static

- static修饰的成员变量和方法，从属于类
- 普通变量和方法从属于对象
- 静态方法不能调用非静态成员，编译会报错

```sh
#static的作用
方便在没有创建对象的情况下进行调用(方法/变量)。
显然，被static关键字修饰的方法或者变量不需要依赖于对象来进行访问，只要类被加载了，就可以通过类名去进行访问。
static可以用来修饰类的成员方法、类的成员变量，另外也可以编写static代码块来优化程序性能
```

```sh
#static方法
static方法也成为静态方法，由于静态方法不依赖于任何对象就可以直接访问，因此对于静态方法来说，是没有this的，因为不依附于任何对象，既然都没有对象，就谈不上this了，并且由于此特性，在静态方法中不能访问类的非静态成员变量和非静态方法，因为非静态成员变量和非静态方法都必须依赖于具体的对象才能被调用。
```

```sh
#static变量
static变量也称为静态变量，静态变量和非静态变量的区别：
    静态变量被所有对象共享，在内存中只有一个副本，在类初次加载的时候才会初始化
    非静态变量是对象所拥有的，在创建对象的时候被初始化，存在多个副本，各个对象拥有的副本互不影响
static成员变量初始化顺序按照定义的顺序来进行初始化
```

```sh
#static块
构造方法用于对象的初始化。静态初始化块，用于类的初始化操作。
在静态初始化块中不能直接访问非staic成员。
静态初始化块的作用就是：提升程序性能。
class Person{
    private Date birthDate;

    public Person(Date birthDate) {
        this.birthDate = birthDate;
    }

    boolean isBornBoomer() {
        Date startDate = Date.valueOf("1946");
        Date endDate = Date.valueOf("1964");
        return birthDate.compareTo(startDate)>=0 && birthDate.compareTo(endDate) < 0;
    }
}
isBornBoomer是用来这个人是否是1946-1964年出生的，而每次isBornBoomer被调用的时候，都会生成startDate和birthDate两个对象，造成了空间浪费，如果改成这样效率会更好：
class Person{
    private Date birthDate;
    private static Date startDate,endDate;
    static{
        startDate = Date.valueOf("1946");
        endDate = Date.valueOf("1964");
    }

    public Person(Date birthDate) {
        this.birthDate = birthDate;
    }

    boolean isBornBoomer() {
        return birthDate.compareTo(startDate)>=0 && birthDate.compareTo(endDate) < 0;
    }
}
因此，很多时候会将一些只需要进行一次的初始化操作都放在static代码块中进行
静态初始化块可以置于类中的任何地方，类中可以有多个静态初始化块。 
在类初次被加载时，会按照静态初始化块的顺序来执行每个块，并且只会执行一次。
原文链接：https://blog.csdn.net/kuangay/article/details/81485324
```

### 12.JVM

##### 1.类的加载

![类加载过程](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-6/类加载过程.png)





### 13.hashCode() 和 equals()

equals() 定义在JDK的Object.java中。通过判断两个对象的地址是否相等(即，是否是同一个对象)来区分它们是否相等。源码如下：

```
public boolean equals(Object obj) {
    return (this == obj);
}
```

使用默认的“**equals()**”方法，等价于“**==**”方法。因此，我们通常会重写equals()方法：若两个对象的内容相等，则equals()方法返回true；否则，返回fasle。

**java对equals()的要求。有以下几点：** 

```
1. 对称性：如果x.equals(y)返回是"true"，那么y.equals(x)也应该返回是"true"。
2. 反射性：x.equals(x)必须返回是"true"。
3. 类推性：如果x.equals(y)返回是"true"，而且y.equals(z)返回是"true"，那么z.equals(x)也应该返回是"true"。
4. 一致性：如果x.equals(y)返回是"true"，只要x和y内容一直不变，不管你重复x.equals(y)多少次，返回都是"true"。
5. 非空性，x.equals(null)，永远返回是"false"；x.equals(和x不同类型的对象)永远返回是"false"。
```



**hashCode()** 

hashCode() 的作用是**获取哈希码**，也称为散列码；它实际上是返回一个int整数。这个**哈希码的作用**是确定该对象在哈希表中的索引位置。hashCode() 定义在JDK的Object.java中，这就意味着Java中的任何类都包含hashCode() 函数。及每个对象都有一个哈希码值，但hashCode() 在散列表中才有用，在其它情况下没用. **散列表指的是：Java集合中本质是散列表的类，如HashMap，Hashtable，HashSet。**

```sh
1.如果两个对象相同，那么它们的hashCode值一定要相同；因此重载其中一个方法时也需要将另一个也重载
2.如果两个对象的hashCode相同，它们并不一定相同（这里说的对象相同指的是用eqauls方法比较）。  
  如不按要求去做了，会发现相同的对象可以出现在Set集合中，同时，增加新元素的效率会大大下降。
3.equals()相等的两个对象，hashcode()一定相等；equals()不相等的两个对象，却并不能证明他们的hashcode()不相等。换句话说，equals()方法不相等的两个对象，hashcode()有可能相等（我的理解是由于哈希码在生成的时候产生冲突造成的）。反过来，hashcode()不等，一定能推出equals()也不等；hashcode()相等，equals()可能相等，也可能不等。
```



### 14.集合

##### List、Set、Map

```sh
List(对付顺序的好帮手)： List接口存储一组不唯一（可以有多个元素引用相同的对象），有序的对象
      Arraylist： Object数组
      Vector： Object数组
      LinkedList： 双向链表
Set(注重独一无二的性质): 不允许重复的集合。不会有多个元素引用相同的对象。
      HashSet（无序，唯一）: 基于 HashMap 实现的，底层采用 HashMap 来保存元素
      LinkedHashSet： LinkedHashSet 继承于 HashSet，并且其内部是通过 LinkedHashMap 来实现的。        有点类似于我们之前说的LinkedHashMap 其内部是基于 HashMap 实现一样，不过还是有一点点区别的
      TreeSet（有序，唯一）： 红黑树(自平衡的排序二叉树
Map(用Key来搜索的专家): 使用键值对存储。Map会维护与Key有关联的值。两个Key可以引用相同的对象，但Key不能重复，典型的Key是String类型，但也可以是任何对象。
       HashMap：JDK1.8以后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为8）时，将链表转        化为红黑树，以减少搜索时间
       LinkedHashMap： LinkedHashMap 继承自 HashMap，所以它的底层仍然是基于拉链式散列结构即由数        组和链表或红黑树组成。另外，LinkedHashMap 在上面结构的基础上，增加了一条双向链表，使得上面的        结构可以保持键值对的插入顺序。同时通过对链表进行相应的操作，实现了访问顺序相关逻辑。
       Hashtable： 数组+链表组成的，数组是 HashMap 的主体，链表则是主要为了解决哈希冲突而存在的
       TreeMap： 红黑树（自平衡的排序二叉树）
```

##### **ArrayList和LinkedList的区别：**

    1. ArrayList是实现了基于动态数组的数据结构，而LinkedList是基于链表的数据结构；
    
    2. 对于随机访问get和set，ArrayList要优于LinkedList，因为LinkedList要移动指针；ArrayList想要get(int index)元素时，直接返回index位置上的元素，而LinkedList需要通过for循环进行查找
    
    3. 对于添加和删除操作add和remove，一般大家都会说LinkedList要比ArrayList快，因为ArrayList要移动数据。 所以当插入的数据量很小时，两者区别不太大，当插入的数据量大时，大约在容量的1/10之前，LinkedList会优于ArrayList，在其后就劣与ArrayList，且越靠近后面越差。所以个人觉得，一般首选用ArrayList，由于LinkedList可以实现栈、队列以及双端队列等数据结构，所以当特定需要时候，使用LinkedList，当然咯，数据量小的时候，两者差不多，视具体情况去选择使用；当数据量大的时候，如果只需要在靠前的部分插入或删除数据，那也可以选用LinkedList，反之选择ArrayList反而效率更高。
##### **HashMap 、 Hashtable、HashSet对比**

```sh
HashMap和Hashtable实现了Map接口，HashSet实现了Set接口。

HashMap 是非线程安全的，HashTable 是线程安全的；HashTable 内部的方法基本都经过synchronized 修饰。（如果你要保证线程安全的话就使用 ConcurrentHashMap 吧！）
对Null key 和Null value的支持： HashMap 中，null 可以作为键，这样的键只有一个，可以有一个或多个键所对应的值为 null。。但是在 HashTable 中 put 进的键值只要有一个 null，直接抛出NullPointerException。
底层数据结构： JDK1.8 以后的 HashMap 在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为8）时，将链表转化为红黑树，以减少搜索时间。Hashtable 没有这样的机制。

#HashSet如何检查重复
HashSet 底层就是基于 HashMap 实现的。当你把对象加入HashSet时，HashSet会先计算对象的hashcode值来判断对象加入的位置，同时也会与其他加入的对象的hashcode值作比较，如果没有相符的hashcode，HashSet会假设对象没有重复出现。但是如果发现有相同hashcode值的对象，这时会调用equals（）方法来检查hashcode相等的对象是否真的相同。如果两者相同，HashSet就不会让加入操作成功。
```

##### HashMap

```sh
HashMap基于hashing原理，我们通过put()和get()方法储存和获取对象。当我们将键值对传递给put()方法时，HashMap 通过 key 的 hashCode 经过扰动函数处理过后得到 hash 值，然后通过 (n - 1) & hash 判断当前元素存放的位置（bucket位置）（这里的 n 指的是数组的长度），如果当前位置存在元素的话，就判断该元素与要存入的元素的 hash 值以及 key 是否相同，如果相同的话，直接覆盖，不相同就通过拉链法解决冲突。当获取对象时，通过键对象的equals()方法找到正确的键值对，然后返回值对象。

所谓扰动函数指的就是 HashMap 的 hash 方法。使用 hash 方法也就是扰动函数是为了防止一些实现比较差的 hashCode() 方法 换句话说使用扰动函数之后可以减少碰撞。

#有关HashMap的一些问答
“如果两个键的hashcode相同，你如何获取值对象？” 当我们调用get()方法，HashMap会使用键对象的hashcode找到bucket位置,找到bucket位置之后，会调用keys.equals()方法去找到链表中正确的节点，最终找到要找的值对象。

为什么String, Integer这样的wrapper类适合作为键？ String, Integer这样的wrapper（包装类）类作为HashMap的键是再适合不过了，而且String最为常用。因为String是不可变的，也是final的，而且已经重写了equals()和hashCode()方法了。其他的wrapper类也有这个特点。不可变性是必要的，因为为了要计算hashCode()，就要防止键值改变，如果键值在放入时和获取时返回不同的hashcode的话，那么就不能从HashMap中找到你想要的对象。不可变性还有其他的优点如线程安全。因为获取对象的时候要用到equals()和hashCode()方法，那么键对象正确的重写这两个方法是非常重要的。如果两个不相等的对象返回不同的hashcode的话，那么碰撞的几率就会小些，这样就能提高HashMap的性能。

“如果HashMap的大小超过了负载因子(load factor)定义的容量，怎么办？”默认的负载因子大小为0.75，也就是说，当一个map填满了75%的bucket时候，和其它集合类(如ArrayList等)一样，将会创建原来HashMap大小的两倍的bucket数组，来重新调整map的大小，并将原来的对象放入新的bucket数组中。这个过程叫作rehashing，因为它调用hash方法找到新的bucket位置。数组的长度扩大了两倍, 如果不进行rehash那么下次查找的时候就找不到对应的数据,多线程情况下,多个线程对同一个map的数据进行rehash, 会引起链表数据的一个循环链表,当你查询这个链表中的数据时, 因为是循环链表, next1 ==> next2 ==> next1 ==> next2 ... 一直这样next下去会形成死锁.【默认的初始容量（容量为HashMap中槽的数目）是16，且实际容量必须是2的整数次幂。如果 new 的时候指定了容量且不是2的幂，实际容量会是最接近(大于)指定容量的2的幂，比如 new HashMap<>(19)，比19大且最接近的2的幂是32，实际容量就是32。】

HashMap 什么时候开辟bucket数组占用内存？HashMap 在 new 后并不会立即分配**bucket数组，而是第一次 put 时初始化使用resize() 函数进行分配。（类似 ArrayList 在第一次 add 时分配空间）

HashMap 和 ConcurrentHashMap的区别？说简单点就是HashMap是线程不安全的，单线程情况下使用；而ConcurrentHashMap是线程安全的，多线程使用！
```

在get和put的过程中，计算下标时，先对hashCode进行hash操作，然后再通过hash值进一步计算下标，如下图所示：

![1574994546824](C:\Users\eaijusn\AppData\Roaming\Typora\typora-user-images\1574994546824.png)

![img](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-6/JDK1.8%E4%B9%8B%E5%90%8E%E7%9A%84HashMap%E5%BA%95%E5%B1%82%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84.jpg)

TreeMap、TreeSet以及JDK1.8之后的HashMap底层都用到了红黑树。红黑树就是为了解决二叉查找树的缺陷，因为二叉查找树在某些情况下会退化成一个线性结构。



### 15.反射

```sh
Java 反射机制在程序运行时，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性。这种动态的获取信息以及动态调用对象的方法的功能称为 java 的反射机制。
```

https://juejin.im/post/598ea9116fb9a03c335a99a4



### 16.java类型的字节位数

|  类型   |  字节   |  bit数   |                           取值范围                           |
| :-----: | :-----: | :------: | :----------------------------------------------------------: |
|  byte   | 1个字节 | 1*8=8位  |                 (-2)的7次方 ~ (2的7次方) - 1                 |
|  short  | 2个字节 | 2*8=16位 |                (-2)的15次方 ~ (2的15次方) - 1                |
|   int   | 4个字节 | 4*8=32位 |                (-2)的31次方 ~ (2的31次方) - 1                |
|  long   | 8个字节 | 8*8=64位 |                (-2)的63次方 ~ (2的63次方) - 1                |
|  float  | 4个字节 | 4*8=32位 |         float 类型的数值有一个后缀 F（例如：3.14F）          |
| double  | 8个字节 | 8*8=64位 |       没有后缀 F 的浮点数值（例如：3.14）默认为 double       |
|  char   | 2个字节 | 2*8=16位 | Java中，只要是字符，不管是数字还是英文还是汉字，都占两个字节。 |
| boolean | 1个字节 | 1*8=8位  |                         false、true                          |



### 17.tomcat

##### **http**

```sh
Http : Http 允许 Web 服务器和浏览器通过 Internet 发送并接受数据, 是一种基于”请求—响应”的协议, 客户端请求一个文件, 服务器端对该请求进行响应. Http 使用可靠的 tcp 连接, tcp 协议默认使用 tcp 80端口, 
在 Http 中, 总是由客户端通过建立连接并发送 http 请求来初始化一个事务的. Web 服务器端并不负责联系客户端或建立一个到客户端的回调连接.客户端或服务器端可提前关闭连接, 例如, 当使用 Web 浏览器浏览网页时, 可以单击浏览器上的 stop 按钮来停止下载文件, 这样就有效的关闭了一个 Web 服务器的 http 连接.

一个 HTTP 请求包含以下三部分:
* 请求方法—-统一资源标识符(Uniform Resource Identifier, URI)——协议/版本
* 请求头
* 实体
POST /examples/default.jsp HTTP/1.1 
Accept: text/plain; text/html 
Accept-Language: en-gb 
Connection: Keep-Alive 
Host: localhost 
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
Content-Length: 33 Content-Type: application/x-www-form-urlencoded Accept-Encoding: gzip, deflate 

lastName=Franks&firstName=Michael  

方法—统一资源标识符(URI)—协议/版本出现在请求的第一行。
POST /examples/default.jsp HTTP/1.1

这里 POST 是请求方法，/examples/default.jsp 是 URI，而 HTTP/1.1 是协议/版本部分。 每个 HTTP 请求可以使用 HTTP 标准里边提到的多种方法之一。HTTP 1.1 支持 7 种类型的请 求：GET, POST, HEAD, OPTIONS, PUT, DELETE 和 TRACE。GET 和 POST 在互联网应用里边最普遍使用的。

URI 完全指明了一个互联网资源。URI 通常是相对服务器的根目录解释的。因此，始终一斜 线/开头。统一资源定位器(URL)其实是一种 URI(查看 http://www.ietf.org/rfc/rfc2396.txt)
来的。该协议版本代表了正在使用的 HTTP 协议的版本。

请求的头部包含了关于客户端环境和请求的主体内容的有用信息。例如它可能包括浏览器设 置的语言，主体内容的长度等等。每个头部通过一个回车换行符(CRLF)来分隔的。

对于 HTTP 请求格式来说，头部和主体内容之间有一个回车换行符(CRLF)是相当重要的。CRLF 告诉HTTP服务器主体内容是在什么地方开始的。在一些互联网编程书籍中，CRLF还被认为是HTTP 请求的第四部分。

在前面一个 HTTP 请求中，主体内容只不过是下面一行：

lastName=Franks&firstName=Michael

实体内容在一个典型的 HTTP 请求中可以很容易的变得更长。
```

##### **servlet**

```sh
#Servlet 是什么？
Java Servlet 是运行在 Web 服务器或应用服务器上的程序，它是作为来自 Web 浏览器或其他 HTTP 客户端的请求和 HTTP 服务器上的数据库或应用程序之间的中间层。

使用 Servlet，您可以收集来自网页表单的用户输入，呈现来自数据库或者其他源的记录，还可以动态创建网页。

Servlet 执行以下主要任务：
    读取客户端（浏览器）发送的显式的数据。这包括网页上的 HTML 表单，或者也可以是来自 applet 或自定义的 HTTP 客户端程序的表单。
    读取客户端（浏览器）发送的隐式的 HTTP 请求数据。这包括 cookies、媒体类型和浏览器能理解的压缩格式等等。
    处理数据并生成结果。这个过程可能需要访问数据库，执行 RMI 或 CORBA 调用，调用 Web 服务，或者直接计算得出对应的响应。
    发送显式的数据（即文档）到客户端（浏览器）。该文档的格式可以是多种多样的，包括文本文件（HTML 或 XML）、二进制文件（GIF 图像）、Excel 等。
    发送隐式的 HTTP 响应到客户端（浏览器）。这包括告诉浏览器或其他客户端被返回的文档类型（例如 HTML），设置 cookies 和缓存参数，以及其他类似的任务。
```

![https://www.runoob.com/wp-content/uploads/2014/07/servlet-arch.jpg](https://www.runoob.com/wp-content/uploads/2014/07/servlet-arch.jpg)

##### tomcat运行原理

```sh
#tomcat 三种部署项目的方法
1）第一种：在tomcat中的conf目录中，在server.xml中的，<host/>节点中添加：

<Context path="/hello"
docBase="D:\eclipse3.2.2forwebtools\workspace\hello\WebRoot" debug="0" privileged="true">
</Context>

path是虚拟路径，docBase是JSP应用程序的物理路径
在Tomcat的配置文件中，一个Web应用就是一个特定的Context，可以通过在server.xml中新建Context里部署一个JSP应用程序

2）第二种：将web项目文件件拷贝到webapps 目录中。
Tomcat的Webapps目录是Tomcat默认的应用目录，当服务器启动时，会加载所有这个目录下的应用。也可以将JSP程序打包成一个war包放在目录下，服务器会自动解开这个war包，并在这个目录下生成一个同名的文件夹。一个war包就是有特性格式的jar包，它是将一个Web程序的所有内容进行压缩得到。具体如何打包，可以使用许多开发工具的IDE环境，如Eclipse、NetBeans、ant、JBuilder等。也可以用cmd命令：jar -cvf applicationname.war package.*；

以上两种方法，Web应用被服务器加载后都会在Tomcat的conf\catalina\localhost目录下生成一个XML文件，你可以直接建一个这样的文件。
3）第三种方法：很灵活，在Tomcat的安装目录下，找到conf\Catalina\localhost
在该目录中新建一个xml文件，名字可以随意取，只要和当前文件中的文件名不重复就行了，该xml文件的内容为：

<Context path="/hello"
docBase="D:\eclipse3.2.2forwebtools\workspace\hello\WebRoot" debug="0"  privileged="true">
</Context>

第3个方法有个优点，可以定义别名。服务器端运行的项目名称为path，外部访问的URL则使用XML的文件名。这个方法很方便的隐藏了项目的名称，对一些项目名称被固定不能更换，但外部访问时又想换个路径，非常有效。

注意：删除一个Web应用同时也要删除webapps下相应的文件夹server.xml中相应的Context，还要将Tomcat的conf
\catalina\localhost目录下相应的xml文件删除。否则Tomcat仍会岸配置去加载。
```

![https://upload-images.jianshu.io/upload_images/6807865-f3747ca11e20e10c.png](https://upload-images.jianshu.io/upload_images/6807865-f3747ca11e20e10c.png)

![https://images2015.cnblogs.com/blog/1078737/201701/1078737-20170109110435088-1621430699.png](https://images2015.cnblogs.com/blog/1078737/201701/1078737-20170109110435088-1621430699.png)

```sh
从上图可以看出Tomcat的核心是两个组件：连接器（Connector）和容器（Container）。Connector组件是负责生成请求对象和响应对象的，Tomcat默认的是HttpConnector，负责根据收到的Http请求报文生成Request对象和Response对象，并把这两个对象传递给Container，然后根据Response中的内容生成相应的HTTP报文。

Container是容器的父接口，所有子容器都必须实现这个接口，简单来说就是服务器部署的项目是运行在Container中的。Container里面的项目获取到Connector传递过来对应的的Request对象和Response对象进行相应的操作。

Connector可以根据不同的设计和应用场景进行替换。一个Container可以选择对应多个Connector。多个Connector和一个Container就形成了一个Service，有了Service就可以对外提供服务了。

#Tomcat要为一个Servlet的请求提供服务，需要做四件事：
 1.创建一个request对象并填充那些有可能被所引用的Servlet使用的信息，如参数，头部、cookies、查询字符串等。一个request对象就是javax.servlet.ServletRequest或javax.servlet.http.ServletRequest接口的一个实例。
 2.创建一个response对象，所引用的servlet使用它来给客户端发送响应。一个response对象是javax.servlet.ServletResponse或javax.servlet.http.ServletResponse接口的一个实例。
 3.调用servlet的service方法，并传入request和response对象。这里servlet会从request对象取值，给response写值。
 4.根据servlet返回的response生成相应的HTTP响应报文。
```



### 18.Netty

##### **NIO**

```sh
#异步
异步与同步相对，当一个异步过程调用发出后，调用者在没有得到结果之前，就可以继续执行后续操作。也就是说无论异步方法执行代码需要多长时间，跟主线程没有任何影响，主线程可以继续向下执行。

互斥：不能同时使用临界资源（有一个共享资源--话筒，两人必须用话筒说话，但同时只能有一个人用这个话筒，保证了只有一个人在说话）
同步：前一个处理的结果作为下一个处理的资源。大多数情况下，同步已经实现了互斥。（两人你一言我一语的交流，我必须知道你说了啥我才能接上你的话）
并行：同一时刻处理多个任务的能力（两人合唱，同时出声）
异步：不用等待一个结果出来，可以继续其他操作（两个人不说话了，寄信，a把信拿到邮局就不用管了，回家可以想干嘛就干嘛，等b回信到了，取邮局接收一下结果--b的回信就可以了）
```

