package thread01;

public class Dog extends Thread{

    @Override
    public void run() {
         for (int i=2;i<100;i++){
             System.out.println("dog run " + i + " steps");
         }
    }
}
